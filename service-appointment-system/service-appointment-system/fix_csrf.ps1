Get-ChildItem -Path "src\main\resources\templates" -Recurse -Filter *.html | ForEach-Object {
    $content = Get-Content $_.FullName
    $content | Where-Object { $_ -notmatch "_csrf" } | Set-Content $_.FullName
}
