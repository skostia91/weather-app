# Реализация 5го проекта Weather на Spring Boot, Data, Cache, AOP, PostgreSQL, FlyWay, Docker.
Без использования Spring Security и Hibernate.

С "ручной" работой c сессией и куками. Проверка сессий происходит через реализацию аоп.

С использование кэширования для локаций популярных и для работы с login user'а через @Cacheable.
Кэширование не реализовано "руками" по причине того, что я не работал с кэширование спринга раньше и хотелось сначала так сделать.


## 1. Структура

### Добавлена миграция бд - flyway.
### Добавлена 3 профиля. dev - для образа в докере, test - для тестов и ide - рабочий(который надо назначать при запуске idea)
### Добавлен dockerfile.
### Добавлены 2 docker-compose файл. dev(для запуска ревьюэра) и prod(для деплоя).
### Добавлен файл .env где находятся константы для docker-compose-prod.yaml файла.

### Добавлен доп.функционал на logout
(Он абсолютно бесполезен и не несёт смысловую нагрузку, но меня он какое-то время веселил).
При ревью необходимо будет прочетать раздел "4. Информация для ревьюэра", там есть информация как её отключить

### Добавлен доп.функционал для запрета некоторого количества слабых паролей(с секретной пасхалкой(тоже бесполезный, но мне не захотелось её оставить)).
### Добавлен Mapstruct.
### Добавлены testcontainer. В pom.xml файле добавлен plugin для того, чтобы тесты которые используют тестконтейнер при сборке были пропущены.
(Тесты для UserService замоканы. Для тестов LocationService и SessionService использован testcontainer)

### Добавлены все зависимости в pom.xml файле в одно место.
### Нет кастомных ошибок, потому что по логике приложения тут не ошибки, а логика программы допускает не правильный ввод данных.
### Во всей прилаге нет модификаторов private и final у полей, по причине хотел попробовать новый функционал.



## 2. Популярные ошибки в прошлых реализациях:
- добавлены индексы для сессий эффективной работы бд(вроде бы правильно)

- создание одинаковых констрейнтов модели как в бд, так и в проекте 
Например: 
в проекте   @Column(name = "login", nullable = false, unique = true)
в бд - login VARCHAR(45) NOT NULL UNIQUE,

- есть проверка в прилаге и бд на размер пароля, для недопустимости слишком короткого
password varchar(205) NOT NULL check(length(password) > 2)

- не userfriendly отображение карточек с температурой. Когда мы ищем Санкт-Петербург, а нам выдаёт Новая Голандия.
В этом варианте будет 
Локация: Санкт-Петербург
Расположение метеостанции: Новая Голандия
Температура: ....

Вроде бы:
1. Не должно быть race conditional, на примере частых ошибок 3го проекта.(которую решал через добавление ключа
на имя, долготу и широту)
2. Не должно быть race conditional, которую решал через локальную переменную как объяснено в ревью Гаранина) 
3. Нет уязвимости как в ревью на 6й минуте у романа где во фронте можно поменять id локации и удалить чужую локацию. 
В логике удаления стоит передача кук в которых передаётся idUser.



## 3. Что не реализовал из частых ошибок
- не выровнял карточки во фронте
- архитектура слабо масштабируется которая была расссказана в ревью криоса
- не реализованы связи через аннотации @OneToMany, @ManyToMany
по причине - увеличение сложности и слабой предсказуемости поведения сущностей
- не реализовал тесты на OpenApiService(чуть позже напишу, если не психану ждать запуска тестконтейнера)



## 4. Информация для ревьюэра
По умолчанию удаление кэша происходит через час.
(информация как изменить время жизни кэша и проверить функционал находится на 85 строке в ApiService и на 46 строке UserService).

По умолчанию жизнь сессии идёт в 50 минут и удаляется после проверки через минуту.
(информация как изменить время жизни и проверить функционал находится на 120 строке в AuthController)

Logout: информация как отключить этот функционал, 
чтобы не тратить на него время и отключить проверки для выхода, находится на 127 строке в LocationController)


## 5. Запуск
В docker-compose-dev.yaml файле стоят захардкоженные значения для более простого запуска ревьюэру.
Запуск должен быть корректен через команды терминала idea
cd docker
docker-compose -f docker-compose-dev.yaml up -d

ввод в браузере 
localhost:8082/weather
