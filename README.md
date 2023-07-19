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
