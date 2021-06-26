[![Build Status](https://travis-ci.com/velesov7493/job4j_grabber.svg?branch=master)](https://travis-ci.com/velesov7493/job4j_grabber)
[![codecov](https://codecov.io/gh/velesov7493/job4j_grabber/branch/master/graph/badge.svg?token=UVF2SIQF30)](https://codecov.io/gh/velesov7493/job4j_grabber)
# job4j grabber #
## Агрегатор Java вакансий ##

В этом задании мы опишем проект - Агрегатор вакансий.
Для этого проекта создайте отдельный репозиторий job4j_grabber.

В нем добавьте maven, travis, jacoco, checkstyle.
Приложение должно собираться в jar.

### Описание ###

Система запускается по расписанию. Период запуска указывается в настройках - app.properties.

Первый сайт будет sql.ru. В нем есть раздел job. Программа должна считывать все вакансии относящиеся к Java и записывать их в базу.

Доступ к интерфейсу будет через REST API.

### Расширение ###

1. В проект можно добавить новые сайты без изменения кода.
2. В проекте можно сделать параллельный парсинг сайтов.