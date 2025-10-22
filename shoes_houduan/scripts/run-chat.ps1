param(
  [switch]$Rebuild,
  [switch]$Detach
)

$ErrorActionPreference = "Stop"

# 切到后端项目根目录（脚本所在目录的上一层）
Set-Location -Path (Join-Path $PSScriptRoot '..')

function Resolve-Maven {
  if (Test-Path .\mvnw.cmd) {
    return "${PWD}\mvnw.cmd"
  }
  if (Get-Command mvn -ErrorAction SilentlyContinue) {
    return "mvn"
  }
  # 本机没有 Maven，则下载临时 Maven 到用户目录
  $mavenVersion = '3.9.11'
  $mavenHome = Join-Path $env:USERPROFILE ".maven\apache-maven-$mavenVersion"
  $mvnCmd = Join-Path $mavenHome 'bin\mvn.cmd'
  if (-not (Test-Path $mvnCmd)) {
    Write-Host "Maven 未检测到，正在下载 $mavenVersion..." -ForegroundColor Yellow
    $zipUrl = "https://dlcdn.apache.org/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
    $zipPath = Join-Path $env:TEMP "apache-maven-$mavenVersion-bin.zip"
    Invoke-WebRequest -Uri $zipUrl -OutFile $zipPath
    New-Item -ItemType Directory -Force -Path (Split-Path $mavenHome) | Out-Null
    Expand-Archive -Path $zipPath -DestinationPath (Split-Path $mavenHome) -Force
  }
  return $mvnCmd
}

$mvn = Resolve-Maven

if ($Rebuild) {
  & $mvn '-q' 'clean' 'package' '-DskipTests'
}

# 以 chat profile 启动
if ($Detach) {
  $cmdLine = "`"$mvn`" -q spring-boot:run -Dspring-boot.run.profiles=chat"
  Start-Process -FilePath 'cmd.exe' -ArgumentList '/c', $cmdLine -WorkingDirectory (Get-Location).Path -WindowStyle Minimized
  Write-Host 'Backend started in detached mode (cmd process). For logs, run without -Detach.' -ForegroundColor Green
}
else {
  & $mvn '-q' 'spring-boot:run' '-Dspring-boot.run.profiles=chat'
}
