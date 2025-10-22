$ErrorActionPreference = 'Stop'
$srcTab = 'c:\Users\gsy_666\Desktop\shoes_app\static\tabbar'
$dst    = 'c:\Users\gsy_666\Desktop\shoes_app\android-native\app\src\main\res\drawable-nodpi'
if (!(Test-Path -LiteralPath $dst)) { New-Item -ItemType Directory -Path $dst | Out-Null }
$tabMap = @{
    '首页.png'          = 'tab_home.png'
    '帆布鞋-copy.png'   = 'tab_category.png'
    '购物车.png'        = 'tab_cart.png'
    '用户中心-copy.png' = 'tab_mine.png'
}
foreach ($k in $tabMap.Keys) {
    $src = Join-Path $srcTab $k
    if (Test-Path -LiteralPath $src) {
        Copy-Item -LiteralPath $src -Destination (Join-Path $dst $tabMap[$k]) -Force
    }
}
Write-Host "Copied tab icons to $dst"