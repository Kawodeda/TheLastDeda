function Make-TempDir {
    [OutputType([string])]

    $tempPath = [System.IO.Path]::GetTempPath()
    $folderName = (New-Guid).ToString("N")
    $newTempFolder = Join-Path -Path $tempPath -ChildPath $folderName

    New-Item -ItemType Directory -Path $newTempFolder | Out-Null

    return $newTempFolder.Trim()
}

Write-Host "Start server-pack build..."

$ResultDir = "./dist"
$Sources = @("./src/shared", "./src/server")
$TempResultDir = Make-TempDir
$TempDatapacksDir = Make-TempDir

& "./merge-dirs.ps1" -ResultDir "$TempResultDir" -Sources $Sources

Write-Host "Merged souce files for server."

& "./pack-datapacks.ps1" -ResultDir $TempDatapacksDir -SourcesDir "../datapacks"

& "./merge-dirs.ps1" -ResultDir (Join-Path $TempResultDir "resourcepacks") -Sources @($TempDatapacksDir)

Write-Host "Copied datapacks."

Write-Host "Writing artifacts..."

if (Test-Path -Path $ResultDir) {
    Remove-Item -Path $ResultDir -Recurse -Force    
}

New-Item -ItemType Directory -Path $ResultDir | Out-Null

Compress-Archive -Path (Join-Path $TempResultDir "\*") -DestinationPath (Join-Path $ResultDir "server")

Write-Host "Wrote server pack archive."

Write-Host "Cleaning up..."

if (Test-Path -Path $TempResultDir) {
    Remove-Item -Path $TempResultDir -Recurse -Force    
}
if (Test-Path -Path $TempDatapacksDir) {
    Remove-Item -Path $TempDatapacksDir -Recurse -Force    
}

Write-Host "Done."
