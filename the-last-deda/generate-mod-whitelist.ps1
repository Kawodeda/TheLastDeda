$outPath = "./whitelist.txt"

Copy-Item -Path "./fabric-api-deps.txt" -Destination $outPath

./extract-mod-id.ps1 -BaseLocation ".\src\client\mods" -ResultPath $outPath -AppendResult
./extract-mod-id.ps1 -BaseLocation ".\src\shared\mods" -ResultPath $outPath -AppendResult

$whitelistContent = Get-Content -Path $outPath -Raw
$whitelistItems = $whitelistContent.Trim().Trim(',') -split "," | ForEach-Object { $_.Trim().Trim('\"') }

$configPath = "./src/server/config/mod_whitelist-config.json"
$configContent = Get-Content -Path $configPath -Raw
$jsonObject = $configContent | ConvertFrom-Json

$jsonObject.CLIENT_MOD_WHITELIST = $whitelistItems

$jsonObject | ConvertTo-Json -Depth 3 | Out-File $configPath -Encoding UTF8
