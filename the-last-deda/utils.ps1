function Make-TempDir {
    [OutputType([string])]

    $tempPath = [System.IO.Path]::GetTempPath()
    $folderName = (New-Guid).ToString("N")
    $newTempFolder = Join-Path -Path $tempPath -ChildPath $folderName

    New-Item -ItemType Directory -Path $newTempFolder | Out-Null

    return $newTempFolder.Trim()
}
