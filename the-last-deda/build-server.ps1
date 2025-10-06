param (
    [Parameter(Mandatory = $true)]
    [string]$Version
)

. "./utils.ps1"

Write-Host "Start server-pack build..."


Write-Host "Generate mod whitelist..."

& "./generate-mod-whitelist.ps1"


Write-Host "Merge souce files for server..."

$ResultDir = "./dist"
$Sources = @("./src/shared", "./src/server")
$TempResultDir = Make-TempDir
$TempDatapacksDir = Make-TempDir

& "./merge-dirs.ps1" -ResultDir "$TempResultDir" -Sources $Sources


Write-Host "Pack datapacks..."

& "./pack-datapacks.ps1" -ResultDir $TempDatapacksDir -SourcesDir "../datapacks"

Write-Host "Copy datapacks..."

& "./merge-dirs.ps1" -ResultDir (Join-Path $TempResultDir "resourcepacks") -Sources @($TempDatapacksDir)


Write-Host "Write artifacts..."

if (Test-Path -Path $ResultDir) {
    Remove-Item -Path $ResultDir -Recurse -Force    
}

New-Item -ItemType Directory -Path $ResultDir | Out-Null

Compress-Archive -Path (Join-Path $TempResultDir "\*") -DestinationPath (Join-Path $ResultDir "server-$Version")

Write-Host "Wrote server pack archive."


Write-Host "Cleaning up..."

if (Test-Path -Path $TempResultDir) {
    Remove-Item -Path $TempResultDir -Recurse -Force    
}
if (Test-Path -Path $TempDatapacksDir) {
    Remove-Item -Path $TempDatapacksDir -Recurse -Force    
}


Write-Host "Done."
