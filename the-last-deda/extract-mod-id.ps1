param (
    [string]$ResultPath,

    [string[]]$Sources,

    [string]$BaseLocation,

    [switch]$AppendResult = $false
)

. "./utils.ps1"

[string[]]$sourceFiles = $null
if (-not ([string]::IsNullOrEmpty($BaseLocation))) {
    if ($Sources -eq $null) {
        $sourceFiles = Get-ChildItem -Path $BaseLocation -File | ForEach-Object { $_.FullName }
    }
    else {
        $sourceFiles = $Sources | ForEach-Object { Join-Path $BaseLocation $_ }
    }
}
else {
    if ($Sources -eq $null) {
        Write-Host "Must specify Sources or BaseLocation"
        exit 1
    }
}

Add-Type -AssemblyName System.IO.Compression.FileSystem

$result = [System.Collections.ArrayList]::new()
foreach ($src in $sourceFiles) {
    $srcAbsolute = (Resolve-Path -Path $src).Path
    $zip = [System.IO.Compression.ZipFile]::OpenRead($srcAbsolute)

    foreach ($entry in $zip.Entries) {
        $fabricPath = "fabric.mod.json"
        if ($entry.FullName -eq $fabricPath) {
            $stream = $entry.Open()
            $reader = New-Object System.IO.StreamReader($stream)
            $content = $reader.ReadToEnd()
            $reader.Close()
            $stream.Close()

            $jsonObject = $content | ConvertFrom-Json

            $modIds = @($jsonObject.id)

            break
        }

        $forgePath = "META-INF/mods.toml"
        if ($entry.FullName -eq $forgePath) {
            $stream = $entry.Open()
            $reader = New-Object System.IO.StreamReader($stream)
            $content = $reader.ReadToEnd()
            $reader.Close()
            $stream.Close()

            $tomlObject = ConvertFrom-Toml -InputObject $content

            $modIds = $tomlObject.mods | ForEach-Object { $_.modId }

            break
        }
    }

    $zip.Dispose()

    $modIds | ForEach-Object { Write-Host """$_""" }
    if (-not ([string]::IsNullOrEmpty($ResultPath))) {
        $modIds | ForEach-Object { $result.Add("""$_""") | Out-Null }
    }
}

if (-not ($result.Count -eq 0)) {
    ($result -join ",`n") + "," | Out-File -FilePath $ResultPath -Append:$AppendResult -Encoding UTF8
}
