param (
    [Parameter(Mandatory = $true)]
    [string]$ResultDir,

    [Parameter(Mandatory = $true)]
    [string[]]$Sources
)

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
