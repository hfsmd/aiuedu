# 🔥 БЫСТРЫЙ ДЕПЛОЙ НА СЕРВЕР

## Скопируй и выполни:

```bash
# 1. Создай .env
echo "DATABASE_PASSWORD=aiueducation123" > .env && echo "SPRING_PROFILES_ACTIVE=prod" >> .env && echo "DATABASE_USERNAME=aiueducation" >> .env && echo "DATABASE_NAME=aiueducation_prod" >> .env

# 2. Останови nginx
sudo systemctl stop nginx 2>/dev/null || true

# 3. Очисти Docker
docker-compose down --remove-orphans 2>/dev/null || true && docker volume rm demo_postgres_data 2>/dev/null || true

# 4. Запусти
docker-compose up --build -d

# 5. Проверь
docker-compose ps && curl -I http://localhost
```

## ✅ Готово! 
**Сайт работает на:** http://your-server-ip

---

### Если что-то не работает:
```bash
# Логи
docker-compose logs app

# Перезапуск
docker-compose restart
``` 