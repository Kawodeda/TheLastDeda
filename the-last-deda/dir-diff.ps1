param (
    [Parameter(Mandatory = $true)]
    [string]$Dir1,

    [Parameter(Mandatory = $true)]
    [string]$Dir2
)

# Ensure directories exist
if (-not (Test-Path $Dir1)) {
    Write-Error "Directory '$Dir1' does not exist."
    exit
}
if (-not (Test-Path $Dir2)) {
    Write-Error "Directory '$Dir2' does not exist."
    exit
}

Write-Host "Comparing directories:" -ForegroundColor Cyan
Write-Host "Dir1: $Dir1" -ForegroundColor Yellow
Write-Host "Dir2: $Dir2" -ForegroundColor Yellow
Write-Host "`n"

# Get file lists with relative paths
$filesDir1 = Get-ChildItem -Path $Dir1
$filesDir2 = Get-ChildItem -Path $Dir2

$relativeDir1 = $filesDir1 | ForEach-Object {
    [PSCustomObject]@{
        RelativePath = $_.FullName.Substring($Dir1.Length).TrimStart('\')
        FullPath     = $_.FullName
    }
}

$relativeDir2 = $filesDir2 | ForEach-Object {
    [PSCustomObject]@{
        RelativePath = $_.FullName.Substring($Dir2.Length).TrimStart('\')
        FullPath     = $_.FullName
    }
}

# Compare file lists
$onlyInDir1 = $relativeDir1.RelativePath | Where-Object { $_ -notin $relativeDir2.RelativePath }
$onlyInDir2 = $relativeDir2.RelativePath | Where-Object { $_ -notin $relativeDir1.RelativePath }
$inBoth     = $relativeDir1.RelativePath | Where-Object { $_ -in $relativeDir2.RelativePath }

Write-Host "Files only in Dir1:" -ForegroundColor Green
$onlyInDir1 | ForEach-Object { Write-Host "  $_" }

Write-Host "`nFiles only in Dir2:" -ForegroundColor Green
$onlyInDir2 | ForEach-Object { Write-Host "  $_" }

Write-Host "`nFiles in both but different content:" -ForegroundColor Green
foreach ($file in $inBoth | Where-Object { -not $_.PSIsContainer }) {
    $file1 = $relativeDir1 | Where-Object { $_.RelativePath -eq $file } | Select-Object -First 1
    $file2 = $relativeDir2 | Where-Object { $_.RelativePath -eq $file } | Select-Object -First 1

    $hash1 = Get-FileHash $file1.FullPath -Algorithm SHA256
    $hash2 = Get-FileHash $file2.FullPath -Algorithm SHA256

    if ($hash1.Hash -ne $hash2.Hash) {
        Write-Host "  $file"
    }
}