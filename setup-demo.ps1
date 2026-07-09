# ============================================================
# 演示数据部署脚本
# 将 demo-assets/ 中的图片复制到 uploads/ 目录
# 用法: .\setup-demo.ps1
# ============================================================

$root = $PSScriptRoot
$src = "$root\demo-assets\image"

if (-not (Test-Path $src)) {
    Write-Output "错误: demo-assets/image/ 目录不存在"
    exit 1
}

# 目标目录：项目根 uploads 和 backend/uploads（后端运行时路径）
$targets = @(
    "$root\uploads\image",
    "$root\backend\uploads\image"
)

foreach ($dst in $targets) {
    if (-not (Test-Path $dst)) {
        New-Item -ItemType Directory -Path $dst -Force | Out-Null
    }

    $copied = 0
    Get-ChildItem $src -Filter "*.jpg" | ForEach-Object {
        $target = Join-Path $dst $_.Name
        if (-not (Test-Path $target)) {
            Copy-Item $_.FullName $target
            $copied++
        }
    }

    $total = (Get-ChildItem $dst -Filter "*.jpg").Count
    Write-Output "$dst -> $copied new, $total total"
}

Write-Output "完成"