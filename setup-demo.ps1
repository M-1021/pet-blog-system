# 演示数据部署脚本
# 将 demo-assets/ 中的文件复制到 uploads/ 目录
# 用法: .\setup-demo.ps1

$root = $PSScriptRoot
$src = "$root\demo-assets"
$dst = "$root\uploads"

if (-not (Test-Path $src)) {
    Write-Output "错误: demo-assets/ 目录不存在"
    exit 1
}

# 确保 uploads 子目录存在
@("image", "video", "avatar") | ForEach-Object {
    $dir = "$dst\$_"
    if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Path $dir -Force | Out-Null }
}

# 复制文件（跳过已存在的）
$copied = 0
Get-ChildItem $src -Recurse -File | ForEach-Object {
    $relative = $_.FullName.Substring($src.Length)
    $target = "$dst$relative"
    if (-not (Test-Path $target)) {
        $targetDir = Split-Path $target -Parent
        if (-not (Test-Path $targetDir)) { New-Item -ItemType Directory -Path $targetDir -Force | Out-Null }
        Copy-Item $_.FullName $target
        Write-Output "  复制: $relative"
        $copied++
    }
}

if ($copied -eq 0) {
    Write-Output "所有演示文件已就绪，无需复制"
} else {
    Write-Output "完成: 复制了 $copied 个文件到 uploads/"
}