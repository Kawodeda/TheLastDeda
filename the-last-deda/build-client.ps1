Write-Host "Start client-pack build..."

$ResultDir = "./dist/client"
$Sources = @("./src/shared", "./src/client")

& "./merge-dirs.ps1" -ResultDir $ResultDir -Sources $Sources

Write-Host "Done."
