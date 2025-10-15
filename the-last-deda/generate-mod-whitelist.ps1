$outPath = "./whitelist.txt"

Copy-Item -Path "./dynamic-mods-list.txt" -Destination $outPath

./extract-mod-id.ps1 -BaseLocation ".\src\client\mods" -ResultPath $outPath -AppendResult
./extract-mod-id.ps1 -BaseLocation ".\src\shared\mods" -ResultPath $outPath -AppendResult
./extract-mod-id.ps1 -BaseLocation ".\src\server\mods" -ResultPath $outPath -AppendResult

Write-Host "Extracted."

$whitelistContent = Get-Content -Path $outPath -Raw
Write-Host "Got whitelist"
$whitelistItems = $whitelistContent.Trim().Trim(',') -split "," | ForEach-Object { $_.Trim().Trim('\"') }
Write-Host "Parsed items"

$configPath = "./src/server/config/mod_whitelist-config.json"
$configContent = Get-Content -Path $configPath -Raw
Write-Host "Got config file"
$jsonObject = $configContent | ConvertFrom-Json
Write-Host "Got config JSON"

$jsonObject.CLIENT_MOD_WHITELIST = $whitelistItems
Write-Host "Set whitelist"
$jsonObject | ConvertTo-Json -Depth 3 | Out-File $configPath -Encoding UTF8
Write-Host "Wrote output"
