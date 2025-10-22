$ErrorActionPreference = "Stop"
$body = @{ username = 'test'; password = '123456' } | ConvertTo-Json
$resp = Invoke-RestMethod -Uri 'http://127.0.0.1:8080/api/auth/login' -Method POST -ContentType 'application/json' -Body $body
$resp | ConvertTo-Json -Compress
