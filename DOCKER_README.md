# PlacementSyncer - Docker Deployment Guide

## Quick Start with Docker 🚀

This guide provides step-by-step instructions to run PlacementSyncer using Docker.

### Prerequisites

- Docker Desktop (Windows/Mac) or Docker Engine (Linux)
- Docker Compose v2.0 or higher
- 4GB+ RAM available for containers
- 2GB+ disk space

### 1. Clone Repository

```bash
git clone https://github.com/RamBelitkar/Placement_expo.git
cd Placement_expo
```

### 2. Environment Configuration

Create a `.env` file in the project root:

```bash
# Copy the template
cp .env.template .env
```

Edit `.env` with your values:

```env
# Database Configuration
POSTGRES_DB=placement_expo
POSTGRES_USER=placement_user
POSTGRES_PASSWORD=your_secure_password_here

# Application Configuration
JWT_SECRET=your-super-secure-jwt-secret-key-256-bits-minimum
APPWRITE_PROJECT_ID=your_appwrite_project_id
APPWRITE_ENDPOINT=https://cloud.appwrite.io/v1

# Upload Configuration
UPLOAD_DIR=/app/uploads
MAX_FILE_SIZE=10MB

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:8080,https://yourdomain.com
```

### 3. Build and Run with Docker Compose

#### Development Environment

```bash
# Build and start all services
docker-compose up --build

# Run in background
docker-compose up -d --build

# View logs
docker-compose logs -f app
```

#### Production Environment

```bash
# Use production configuration
docker-compose -f docker-compose.prod.yml up --build -d

# Check status
docker-compose -f docker-compose.prod.yml ps

# View application logs
docker-compose -f docker-compose.prod.yml logs app
```

### 4. Verify Deployment

Once containers are running:

1. **Application**: http://localhost:8080
2. **Health Check**: http://localhost:8080/actuator/health
3. **API Documentation**: http://localhost:8080/swagger-ui.html
4. **Database**: postgresql://localhost:5432/placement_expo

### 5. Initial Setup

1. **Register a User**:
   ```bash
   curl -X POST http://localhost:8080/api/v1/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "name": "Test User",
       "email": "test@example.com",
       "appwriteId": "test-user-123"
     }'
   ```

2. **Create Profile**:
   Navigate to http://localhost:8080/register.html and complete registration

3. **Upload Resume**:
   Use the dashboard to upload and analyze your resume

### 6. Docker Commands Reference

```bash
# View running containers
docker-compose ps

# Stop all services
docker-compose down

# Rebuild specific service
docker-compose build app

# Remove all data (destructive)
docker-compose down -v

# Access application container
docker-compose exec app bash

# Access database
docker-compose exec postgres psql -U placement_user -d placement_expo

# View logs for specific service
docker-compose logs postgres
docker-compose logs app
```

### 7. Troubleshooting

#### Container won't start
```bash
# Check logs
docker-compose logs app

# Rebuild without cache
docker-compose build --no-cache app
```

#### Database connection issues
```bash
# Verify database is running
docker-compose ps postgres

# Check database logs
docker-compose logs postgres

# Test connection
docker-compose exec postgres pg_isready -U placement_user
```

#### Port conflicts
```bash
# Check what's using port 8080
netstat -tulpn | grep 8080

# Use different port
sed -i 's/8080:8080/8081:8080/' docker-compose.yml
```

### 8. Backup and Restore

#### Backup Database
```bash
docker-compose exec postgres pg_dump -U placement_user placement_expo > backup.sql
```

#### Restore Database
```bash
docker-compose exec -T postgres psql -U placement_user placement_expo < backup.sql
```

### 9. Production Deployment

For production deployment:

1. **Use production compose file**: `docker-compose.prod.yml`
2. **Set strong passwords** in `.env`
3. **Configure SSL/TLS** via reverse proxy
4. **Set up monitoring** and log aggregation
5. **Configure backup strategy**

#### Example Nginx Configuration
```nginx
server {
    listen 443 ssl;
    server_name your-domain.com;
    
    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### 10. Monitoring

#### Health Checks
```bash
# Application health
curl http://localhost:8080/actuator/health

# Database health
docker-compose exec postgres pg_isready -U placement_user

# Container stats
docker stats
```

#### Logs
```bash
# Application logs
docker-compose logs -f app

# Database logs
docker-compose logs -f postgres

# All services
docker-compose logs -f
```

### Environment Variables Reference

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `POSTGRES_DB` | Database name | placement_expo | Yes |
| `POSTGRES_USER` | Database user | placement_user | Yes |
| `POSTGRES_PASSWORD` | Database password | - | Yes |
| `JWT_SECRET` | JWT signing key | - | Yes |
| `APPWRITE_PROJECT_ID` | Appwrite project ID | - | Yes |
| `APPWRITE_ENDPOINT` | Appwrite API endpoint | - | Yes |
| `UPLOAD_DIR` | File upload directory | /app/uploads | No |
| `CORS_ALLOWED_ORIGINS` | Allowed CORS origins | localhost:8080 | No |

---

## Support

For issues and questions:
- Create an issue on GitHub
- Check logs using `docker-compose logs`
- Verify environment configuration
- Review Docker and Docker Compose documentation
