---
name: no-bom-utf8
description: Enforce BOM-free UTF-8 for all file writes. This skill must always be active when writing source code files (.java, .py, .js, .ts, .vue, .html, .css, .json, .xml, .yml, .yaml, .sql, .md, .sh, .bat, .properties, .cfg, .ini, .conf, .toml). Use this skill whenever creating or editing any source code file to prevent UTF-8 BOM encoding issues.
---

# No-BOM UTF-8

## Rule

When writing or creating source code files, always use BOM-free UTF-8 encoding.

### In PowerShell

Use `[System.IO.File]::WriteAllText()` with a BOM-free encoder:

```powershell
[System.IO.File]::WriteAllText($path, $content, [System.Text.UTF8Encoding]::new($false))
```

Or write raw bytes without BOM:

```powershell
[System.IO.File]::WriteAllBytes($path, [System.Text.Encoding]::UTF8.GetBytes($content))
```

Never use these for source code files:

```powershell
# BAD - adds BOM in Windows PowerShell 5.x
Out-File -Encoding UTF8
Set-Content -Encoding UTF8
```

### In Node.js / JavaScript

`fs.writeFileSync()` and `fs.writeFile()` default to UTF-8 without BOM. Use them directly:

```js
fs.writeFileSync(path, content, "utf8");
```

### In Python

Python `open()` writes UTF-8 without BOM by default:

```python
with open(path, "w", encoding="utf-8") as f:
    f.write(content)
```

### Batch strip BOM from existing files

```powershell
Get-ChildItem -Recurse -Filter "*.java" | ForEach-Object {
    $bytes = [IO.File]::ReadAllBytes($_.FullName)
    if ($bytes.Length -ge 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
        [IO.File]::WriteAllBytes($_.FullName, $bytes[3..($bytes.Length-1)])
    }
}
```