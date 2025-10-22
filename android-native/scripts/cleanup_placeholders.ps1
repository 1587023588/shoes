$ErrorActionPreference = 'Stop'
$dir = 'c:\Users\gsy_666\Desktop\shoes_app\android-native\app\src\main\res\drawable'
$files = @(
    'home_banner_img.xml',
    'splash_mascot_img.xml',
    'product_img_1.xml',
    'product_img_2.xml',
    'product_img_3.xml',
    'product_img_4.xml',
    'product_img_5.xml',
    'product_img_6.xml'
)
foreach ($f in $files) {
    $p = Join-Path $dir $f
    if (Test-Path -LiteralPath $p) {
        Remove-Item -LiteralPath $p -Force
    }
}
Write-Host 'Removed placeholder XML drawables.'