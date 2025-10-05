Write-Host "Start server-pack build..."

$ResultDir = "./dist/server"
$Sources = @("./src/shared", "./src/server")

if (Test-Path -Path $ResultDir) {
    Remove-Item -Path $ResultDir -Recurse -Force    
}

New-Item -ItemType Directory -Path $ResultDir | Out-Null

foreach ($src in $Sources) {
    $srcAbsolute = (Resolve-Path -Path $src).Path
    Get-ChildItem -Path $srcAbsolute -Recurse -File | ForEach-Object {
        $relativePath = $_.FullName.Substring($srcAbsolute.Length).TrimStart('\')
        $targetPath = Join-Path $ResultDir $relativePath

        # Ensure target subdirectory exists
        $targetDir = Split-Path $targetPath

        if (-not (Test-Path $targetDir)) {
            New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
        }

        Copy-Item -Path $_.FullName -Destination $targetPath
    }
}

Write-Host "Done."
