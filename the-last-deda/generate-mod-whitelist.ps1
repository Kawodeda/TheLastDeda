./extract-mod-id.ps1 -BaseLocation ".\src\client\mods" -ResultPath "./whitelist.txt"
./extract-mod-id.ps1 -BaseLocation ".\src\shared\mods" -ResultPath "./whitelist.txt" -AppendResult

$whitelistContent = Get-Content -Path "./whitelist.txt" -Raw
$whitelistItems = $whitelistContent.Trim().Trim(',') -split "," | ForEach-Object { $_.Trim().Trim('\"') }

$configPath = "./src/server/config/mod_whitelist-config.json"
$configContent = Get-Content -Path $configPath -Raw
$jsonObject = $configContent | ConvertFrom-Json

$jsonObject.CLIENT_MOD_WHITELIST = $whitelistItems

$jsonObject | ConvertTo-Json -Depth 3 | Out-File $configPath -Encoding UTF8
