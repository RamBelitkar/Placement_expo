# Placement Expo - Deployment Guide

## 🚀 Production Deployment

### Prerequisites

1. **Java 17+** - Required for running the application
2. **PostgreSQL 13+** - Production database
3. **Docker & Docker Compose** - For containerized deployment
4. **SSL Certificate** - For HTTPS (recommended)
5. **Domain Name** - For production access

### Environment Variables

Copy `.env.template` to `.env` and configure the following variables:

```bash
# Database Configuration
DATABASE_URL=jdbc:postgresql://your-db-host:5432/placement_expo
DATABASE_USERNAME=your_db_user
DATABASE_PASSWORD=your_secure_db_password

# JWT Security - Generate a secure 256-bit key
JWT_SECRET=your-very-long-and-secure-jwt-secret-key-here-minimum-256-bits
JWT_EXPIRATION=86400000

# Appwrite Configuration
APPWRITE_ENDPOINT=https://nyc.cloud.appwrite.io/v1
APPWRITE_PROJECT_ID=your_appwrite_project_id

# CORS Configuration
CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://www.yourdomain.com

# File Upload Directory
UPLOAD_DIR=/app/uploads

# Optional: Sentry Error Monitoring
SENTRY_DSN=https://your-sentry-dsn@sentry.io/project-id

# SSL Configuration (if using HTTPS)
SSL_ENABLED=true
SSL_KEYSTORE=/path/to/keystore.p12
SSL_KEYSTORE_PASSWORD=your_keystore_password
SSL_KEYSTORE_TYPE=PKCS12

# Server Configuration
PORT=8080
ENVIRONMENT=production
```

### Deployment Options

#### Option 1: Docker Deployment (Recommended)

1. **Build the Docker image:**
   ```bash
   docker build -t placement-expo:latest .
   ```

2. **Run with Docker Compose:**
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

3. **Check logs:**
   ```bash
   docker-compose logs -f app
   ```

#### Option 2: JAR Deployment

1. **Build the application:**
   ```bash
   ./mvnw clean package -Pproduction
   ```

2. **Run the application:**
   ```bash
   java -Dspring.profiles.active=prod -jar target/expo-0.0.1-SNAPSHOT.jar
   ```

#### Option 3: Cloud Platform Deployment

##### Heroku Deployment

1. **Create a Heroku app:**
   ```bash
   heroku create placement-expo-app
   ```

2. **Add PostgreSQL addon:**
   ```bash
   heroku addons:create heroku-postgresql:hobby-dev
   ```

3. **Set environment variables:**
   ```bash
   heroku config:set SPRING_PROFILES_ACTIVE=prod
   heroku config:set JWT_SECRET=your-jwt-secret
   heroku config:set APPWRITE_PROJECT_ID=your-project-id
   ```

4. **Deploy:**
   ```bash
   git push heroku main
   ```

##### AWS Deployment

1. **Create an RDS PostgreSQL instance**
2. **Create an EC2 instance or use Elastic Beanstalk**
3. **Configure environment variables**
4. **Deploy the JAR file or Docker image**

### Database Setup

1. **Create PostgreSQL database:**
   ```sql
   CREATE DATABASE placement_expo;
   CREATE USER placement_user WITH PASSWORD 'secure_password';
   GRANT ALL PRIVILEGES ON DATABASE placement_expo TO placement_user;
   ```

2. **The application will automatically run Flyway migrations on startup**

### SSL/HTTPS Setup

1. **Obtain SSL certificate** (Let's Encrypt, CloudFlare, etc.)
2. **Configure SSL in application-prod.properties:**
   ```properties
   server.ssl.enabled=true
   server.ssl.key-store=classpath:keystore.p12
   server.ssl.key-store-password=your_password
   server.ssl.key-store-type=PKCS12
   ```

### Monitoring and Health Checks

- **Health Check Endpoint:** `GET /actuator/health`
- **Metrics Endpoint:** `GET /actuator/metrics`
- **Application Info:** `GET /actuator/info`

### Security Considerations

1. **Always use HTTPS in production**
2. **Use strong, unique passwords for database**
3. **Generate secure JWT secret (256-bit minimum)**
4. **Keep Appwrite credentials secure**
5. **Configure firewall rules appropriately**
6. **Regularly update dependencies**
7. **Enable Sentry for error monitoring**

### Backup Strategy

1. **Database Backups:**
   ```bash
   pg_dump -h your-db-host -U your-user placement_expo > backup.sql
   ```

2. **File Uploads Backup:**
   ```bash
   tar -czf uploads-backup.tar.gz /app/uploads
   ```

### Troubleshooting

#### Common Issues

1. **Database Connection Error:**
   - Check DATABASE_URL format
   - Verify database is running and accessible
   - Check credentials

2. **File Upload Issues:**
   - Ensure UPLOAD_DIR exists and is writable
   - Check disk space
   - Verify file size limits

3. **CORS Errors:**
   - Update CORS_ALLOWED_ORIGINS with your domain
   - Check protocol (HTTP vs HTTPS)

4. **JWT Token Issues:**
   - Verify JWT_SECRET is properly set
   - Check token expiration settings

#### Logs

- **Application logs:** `/app/logs/placement-expo.log`
- **Docker logs:** `docker logs placement-expo-app`
- **System logs:** `/var/log/`

### Performance Optimization

1. **JVM Tuning:**
   ```bash
   -XX:+UseG1GC -XX:MaxRAMPercentage=75.0
   ```

2. **Database Indexing:**
   - Indexes are automatically created by migrations
   - Monitor slow queries

3. **Caching:**
   - Consider adding Redis for session storage
   - Implement HTTP caching headers

### Maintenance

1. **Regular Updates:**
   - Update dependencies monthly
   - Monitor security advisories

2. **Database Maintenance:**
   - Regular backups
   - Monitor disk usage
   - Analyze query performance

3. **Log Rotation:**
   - Configure logrotate for application logs
   - Monitor log disk usage

## 🔧 Development Setup

### Local Development with H2

1. **Clone the repository**
2. **Run with default profile:**
   ```bash
   ./mvnw spring-boot:run
   ```
3. **Access H2 Console:** http://localhost:8081/h2-console

### Local Development with PostgreSQL

1. **Start PostgreSQL with Docker:**
   ```bash
   docker-compose up postgres
   ```

2. **Run application with docker profile:**
   ```bash
   ./mvnw spring-boot:run -Dspring.profiles.active=docker
   ```

### Testing

```bash
# Run unit tests
./mvnw test

# Run integration tests
./mvnw verify

# Generate test report
./mvnw jacoco:report
```

## 📞 Support

For deployment issues or questions:
- Check the logs first
- Review this documentation
- Contact the development team
