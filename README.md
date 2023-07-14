
Путь к сваггер http://localhost:8090/swagger-ui/index.html

Создание нового образа согласно dockerfile

`docker build -t "gran-docs:v1" .`

Запуск контейнера

`docker run -d -p 8671:8671 docker.io/library/gran-docs:v1`