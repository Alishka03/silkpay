# silkpay
Откройте <em>http://localhost:8080/h2-console</em> для того чтобы контролировать БД.<br>
url:jdbc:h2:mem:testdb<br>
password:sa<br>
<br>
Откройте :<code>http://localhost:8080/auth/sign-up</code> для того чтобы авторизоваться.POST запрос:Body->raw_>JSON:<br>
{
   "username":"username",
   "password":"password"
}
<br>
После этого: <code>http://localhost:8080/auth/sign-in</code> для того чтобы войти в систему.POST : Body->raw_>JSON:<br>
{
   "username":"username",
   "password":"password"
}
Получите такой ответ:<br>
<code>
{
    "status": "SUCCESS",
    "id": 4,
    "username": "username",
    "tokenType": "Bearer",
    "accessToken": "some token"
}
</code><br>Копируйте accessToken для того чтобы аутенифицироваться в дальшейших действиях:Authorization->Bearer <br>
<code>http://localhost:8080/my-accounts</code> - для того чтобы увидеть мои счета<br>
<code>http://localhost:8080/new-account</code> - для того чтобы открыть новый счет вы должны указать начальный баланс : RequestBody: <code>{
    "balance":"4000"
}</code> 
<code></code>
