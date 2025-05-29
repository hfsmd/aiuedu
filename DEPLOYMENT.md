# AIUEducation - Deployment Guide

## Деплой на freelance.com.kz

Это руководство поможет вам развернуть платформу онлайн-обучения AIUEducation на вашем домене freelance.com.kz.

## Предварительные требования

### Системные требования
- Ubuntu 20.04+ или CentOS 8+
- Docker 20.10+
- Docker Compose 2.0+
- Минимум 2GB RAM
- Минимум 20GB свободного места на диске
- Открытые порты: 80, 443, 8080

### Установка Docker (Ubuntu)
```bash
# Обновление системы
sudo apt update && sudo apt upgrade -y

# Установка Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Добавление пользователя в группу docker
sudo usermod -aG docker $USER

# Установка Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Перезагрузка для применения изменений
sudo reboot
```

## Быстрый деплой

### 1. Клонирование проекта
```bash
git clone <your-repository-url>
cd aiueducation
```

### 2. Настройка домена
Убедитесь, что ваш домен freelance.com.kz и www.freelance.com.kz указывают на IP-адрес вашего сервера.

### 3. Запуск деплоя
```bash
# Сделать скрипт исполняемым
chmod +x deploy.sh

# Запустить деплой
./deploy.sh deploy
```

## Ручная настройка

### 1. Настройка переменных окружения
```bash
# Создать файл .env
cat > .env << EOF
DB_PASSWORD=your_secure_database_password
SPRING_PROFILES_ACTIVE=prod
DOMAIN=freelance.com.kz
JWT_SECRET=your_jwt_secret_key
EOF
```

### 2. SSL сертификаты

#### Для продакшена (Let's Encrypt)
```bash
# Установка Certbot
sudo apt install certbot

# Получение сертификата
sudo certbot certonly --standalone -d freelance.com.kz -d www.freelance.com.kz

# Копирование сертификатов
sudo cp /etc/letsencrypt/live/freelance.com.kz/fullchain.pem ssl/freelance.com.kz.crt
sudo cp /etc/letsencrypt/live/freelance.com.kz/privkey.pem ssl/freelance.com.kz.key
sudo chown $USER:$USER ssl/*
```

#### Для разработки (самоподписанный)
```bash
mkdir -p ssl
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout ssl/freelance.com.kz.key \
    -out ssl/freelance.com.kz.crt \
    -subj "/C=KZ/ST=Almaty/L=Almaty/O=AIUEducation/CN=freelance.com.kz"
```

### 3. Запуск сервисов
```bash
# Сборка и запуск
docker-compose -f docker-compose.prod.yml up --build -d

# Проверка статуса
docker-compose -f docker-compose.prod.yml ps
```

## Управление приложением

### Команды деплоя
```bash
# Деплой
./deploy.sh deploy

# Просмотр статуса
./deploy.sh status

# Просмотр логов
./deploy.sh logs

# Создание бэкапа
./deploy.sh backup

# Остановка сервисов
./deploy.sh stop

# Перезапуск сервисов
./deploy.sh restart
```

### Мониторинг
- **Главная страница**: https://freelance.com.kz
- **Health Check**: https://freelance.com.kz/health
- **Метрики**: https://freelance.com.kz/actuator/metrics
- **Информация о приложении**: https://freelance.com.kz/actuator/info

### Логи
```bash
# Логи приложения
docker-compose -f docker-compose.prod.yml logs app

# Логи базы данных
docker-compose -f docker-compose.prod.yml logs db

# Логи Nginx
docker-compose -f docker-compose.prod.yml logs nginx

# Все логи в реальном времени
docker-compose -f docker-compose.prod.yml logs -f
```

## Бэкап и восстановление

### Создание бэкапа
```bash
# Автоматический бэкап
./deploy.sh backup

# Ручной бэкап
docker-compose -f docker-compose.prod.yml exec db pg_dump -U aiueducation aiueducation_prod > backup.sql
```

### Восстановление из бэкапа
```bash
# Восстановление базы данных
docker-compose -f docker-compose.prod.yml exec -T db psql -U aiueducation -d aiueducation_prod < backup.sql
```

## Обновление приложения

### 1. Создание бэкапа
```bash
./deploy.sh backup
```

### 2. Обновление кода
```bash
git pull origin main
```

### 3. Перезапуск с новой версией
```bash
./deploy.sh deploy
```

## Настройка производительности

### Настройка JVM
Отредактируйте `docker-compose.prod.yml` и добавьте JVM параметры:
```yaml
environment:
  - JAVA_OPTS=-Xmx2g -Xms1g -XX:+UseG1GC
```

### Настройка PostgreSQL
Создайте файл `postgresql.conf` для оптимизации базы данных:
```bash
# В docker-compose.prod.yml добавьте volume
volumes:
  - ./postgresql.conf:/etc/postgresql/postgresql.conf
```

## Безопасность

### Firewall
```bash
# Установка UFW
sudo ufw enable
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw deny 8080/tcp  # Закрыть прямой доступ к приложению
```

### Обновление паролей
```bash
# Изменение пароля базы данных
docker-compose -f docker-compose.prod.yml exec db psql -U postgres -c "ALTER USER aiueducation PASSWORD 'new_password';"

# Обновление .env файла
sed -i 's/DB_PASSWORD=.*/DB_PASSWORD=new_password/' .env

# Перезапуск сервисов
./deploy.sh restart
```

## Устранение неполадок

### Проверка состояния сервисов
```bash
# Статус контейнеров
docker-compose -f docker-compose.prod.yml ps

# Проверка здоровья
curl -f https://freelance.com.kz/health
```

### Общие проблемы

#### Приложение не запускается
```bash
# Проверка логов
docker-compose -f docker-compose.prod.yml logs app

# Проверка подключения к базе данных
docker-compose -f docker-compose.prod.yml exec app nc -z db 5432
```

#### Проблемы с SSL
```bash
# Проверка сертификатов
openssl x509 -in ssl/freelance.com.kz.crt -text -noout

# Тест SSL соединения
openssl s_client -connect freelance.com.kz:443
```

#### Проблемы с базой данных
```bash
# Подключение к базе данных
docker-compose -f docker-compose.prod.yml exec db psql -U aiueducation -d aiueducation_prod

# Проверка размера базы данных
docker-compose -f docker-compose.prod.yml exec db psql -U aiueducation -d aiueducation_prod -c "\l+"
```

## Контакты и поддержка

Для получения поддержки обратитесь к администратору системы или создайте issue в репозитории проекта.

---

**Важно**: Обязательно сохраните пароли базы данных и другие секретные данные в безопасном месте! 