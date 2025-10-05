param (
    [Parameter(Mandatory = $true)]
    [string]$ResultDir,

    [Parameter(Mandatory = $true)]
    [string[]]$SourcesDir
)

Write-Host "Packing datapacks..."

if (Test-Path -Path $ResultDir) {
    Remove-Item -Path $ResultDir -Recurse -Force    
}

New-Item -ItemType Directory -Path $ResultDir | Out-Null

$SourcesAbsolute = (Resolve-Path -Path $SourcesDir).Path
Get-ChildItem -Path $SourcesAbsolute -Directory | ForEach-Object {
    $VersionFile = Join-Path $_.FullName "\version.txt"
    $Version = Get-Content -Path $VersionFile -First 1
    $SourcePath = Join-Path $_.FullName "\*"
    $DestinationPath = Join-Path $ResultDir -ChildPath ($_.Name + "-" + $Version)

    Write-Host "Writing $DestinationPath"

    Compress-Archive -Path $SourcePath -DestinationPath $DestinationPath
}

Write-Host "Done packing datapacks."
