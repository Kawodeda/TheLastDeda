Write-Host "Start server-pack build..."

$ResultDir = "./dist/server"
$Sources = @("./src/shared", "./src/server")

& "./merge-dirs.ps1" -ResultDir $ResultDir -Sources $Sources

Write-Host "Done."
