$ErrorActionPreference = 'Stop'

$srcImg = 'c:\Users\gsy_666\Desktop\shoes_app\img'
$srcTab = 'c:\Users\gsy_666\Desktop\shoes_app\static\tabbar'
$dst    = 'c:\Users\gsy_666\Desktop\shoes_app\android-native\app\src\main\res\drawable-nodpi'

if (!(Test-Path -LiteralPath $dst)) {
    New-Item -ItemType Directory -Path $dst | Out-Null
}

# Splash & banner
$map = @{
    '1.png' = 'splash_mascot_img.png'
    '2.png' = 'home_banner_img.png'
    '3.png' = 'product_img_1.png'
    '4.png' = 'product_img_2.png'
    '5.png' = 'product_img_3.png'
    '6.png' = 'product_img_4.png'
    '7.png' = 'product_img_5.png'
    '8.png' = 'product_img_6.png'
}

foreach ($k in $map.Keys) {
    $src = Join-Path $srcImg $k
    if (Test-Path -LiteralPath $src) {
        $dstFile = Join-Path $dst $map[$k]
        Copy-Item -LiteralPath $src -Destination $dstFile -Force
    }
}

# Bottom tabs
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

Write-Host "Copied assets to $dst"