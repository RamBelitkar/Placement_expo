# 🚀 PlacementSyncer Deployment Guide

## 📋 Quick Deployment Options

### 1. **Railway (Recommended - Free Tier Available)** 
🟢 **Best for beginners, auto-deploys from GitHub**

#### Steps:
```bash
# 1. Push your code to GitHub
git add .
git commit -m "Ready for deployment"
git push origin main

# 2. Go to https://railway.app
# 3. Sign up with GitHub
# 4. Click "New Project" → "Deploy from GitHub repo"
# 5. Select your Placement_expo repository
# 6. Railway will auto-detect it as a Java/Spring Boot app
```

#### Environment Variables to Set in Railway:
```env
SPRING_PROFILES_ACTIVE=production
DATABASE_URL=postgresql://user:password@hostname:port/database
JWT_SECRET=your-long-secure-jwt-secret-key-256-bits
APPWRITE_PROJECT_ID=6894d20300386b659e32
APPWRITE_ENDPOINT=https://nyc.cloud.appwrite.io/v1
UPLOAD_DIR=/app/uploads
CORS_ALLOWED_ORIGINS=https://yourapp.railway.app
```

**Cost: FREE for 500 hours/month + $5/month for database**

---

### 2. **Render (Free Tier)** 
🟢 **Great free option with PostgreSQL**

#### Steps:
```bash
# 1. Create render.yaml in project root
cat > render.yaml << 'EOF'
services:
  - type: web
    name: placement-expo
    runtime: java
    buildCommand: ./mvnw clean package -DskipTests
    startCommand: java -jar target/placement_expo-0.0.1-SNAPSHOT.jar
    envVars:
      - key: SPRING_PROFILES_ACTIVE
        value: production
      - key: DATABASE_URL
        fromDatabase:
          name: placement-db
          property: connectionString
      - key: JWT_SECRET
        generateValue: true
      - key: APPWRITE_PROJECT_ID
        value: 6894d20300386b659e32
      - key: APPWRITE_ENDPOINT
        value: https://nyc.cloud.appwrite.io/v1

databases:
  - name: placement-db
    databaseName: placement_expo
    user: placement_user
EOF

# 2. Push to GitHub
git add render.yaml
git commit -m "Add Render deployment config"
git push origin main

# 3. Go to https://render.com
# 4. Connect GitHub and deploy
```

**Cost: FREE (with limitations)**

---

### 3. **Heroku (Easy but Paid)** 
🟡 **No free tier anymore, but very reliable**

#### Steps:
```bash
# 1. Install Heroku CLI
# Download from: https://devcenter.heroku.com/articles/heroku-cli

# 2. Login and create app
heroku login
heroku create your-placement-app

# 3. Add PostgreSQL
heroku addons:create heroku-postgresql:essential-0

# 4. Set environment variables
heroku config:set SPRING_PROFILES_ACTIVE=production
heroku config:set JWT_SECRET=your-long-secure-jwt-secret
heroku config:set APPWRITE_PROJECT_ID=6894d20300386b659e32
heroku config:set APPWRITE_ENDPOINT=https://nyc.cloud.appwrite.io/v1

# 5. Create Procfile
echo 'web: java -jar target/placement_expo-0.0.1-SNAPSHOT.jar --server.port=$PORT' > Procfile

# 6. Deploy
git add Procfile
git commit -m "Add Heroku Procfile"
git push heroku main
```

**Cost: $7/month for app + $9/month for database**

---

### 4. **DigitalOcean App Platform** 
🟡 **Good performance, moderate cost**

#### Steps:
```bash
# 1. Create .do/app.yaml
mkdir -p .do
cat > .do/app.yaml << 'EOF'
name: placement-expo
services:
- name: api
  source_dir: /
  github:
    repo: RamBelitkar/Placement_expo
    branch: main
  run_command: java -jar target/placement_expo-0.0.1-SNAPSHOT.jar
  environment_slug: java
  instance_count: 1
  instance_size_slug: basic-xxs
  envs:
  - key: SPRING_PROFILES_ACTIVE
    value: production
  - key: DATABASE_URL
    value: ${placement-db.DATABASE_URL}
  - key: JWT_SECRET
    value: your-jwt-secret-here
  - key: APPWRITE_PROJECT_ID
    value: 6894d20300386b659e32

databases:
- name: placement-db
  engine: PG
  production: false
  version: "15"
EOF

# 2. Push to GitHub
git add .do/app.yaml
git commit -m "Add DigitalOcean deployment config"
git push origin main

# 3. Go to https://cloud.digitalocean.com/apps
# 4. Create new app from GitHub repository
```

**Cost: $12/month for app + $15/month for database**

---

## 🐳 Docker Deployment (Any Cloud Provider)

### Build and Deploy Container

```bash
# 1. Build Docker image
docker build -t placement-expo:latest .

# 2. Test locally
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=production \
  -e DATABASE_URL=your_db_url \
  -e JWT_SECRET=your_secret \
  placement-expo:latest

# 3. Push to container registry (choose one):

# Docker Hub
docker tag placement-expo:latest yourusername/placement-expo:latest
docker push yourusername/placement-expo:latest

# AWS ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 123456789.dkr.ecr.us-east-1.amazonaws.com
docker tag placement-expo:latest 123456789.dkr.ecr.us-east-1.amazonaws.com/placement-expo:latest
docker push 123456789.dkr.ecr.us-east-1.amazonaws.com/placement-expo:latest

# Google Container Registry
docker tag placement-expo:latest gcr.io/your-project-id/placement-expo:latest
docker push gcr.io/your-project-id/placement-expo:latest
```

### Deploy on Different Platforms:

#### **AWS ECS (Elastic Container Service)**
```bash
# 1. Create task definition (placement-task.json)
{
  "family": "placement-expo",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "256",
  "memory": "512",
  "containerDefinitions": [
    {
      "name": "placement-app",
      "image": "123456789.dkr.ecr.us-east-1.amazonaws.com/placement-expo:latest",
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {"name": "SPRING_PROFILES_ACTIVE", "value": "production"},
        {"name": "DATABASE_URL", "value": "your_rds_url"},
        {"name": "JWT_SECRET", "value": "your_secret"}
      ]
    }
  ]
}

# 2. Deploy
aws ecs register-task-definition --cli-input-json file://placement-task.json
aws ecs create-service --cluster your-cluster --service-name placement-service --task-definition placement-expo
```

#### **Google Cloud Run**
```bash
# Deploy to Cloud Run
gcloud run deploy placement-expo \
  --image gcr.io/your-project-id/placement-expo:latest \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars SPRING_PROFILES_ACTIVE=production,DATABASE_URL=your_db_url,JWT_SECRET=your_secret
```

#### **Azure Container Instances**
```bash
# Deploy to Azure
az container create \
  --resource-group myResourceGroup \
  --name placement-expo \
  --image yourusername/placement-expo:latest \
  --cpu 1 \
  --memory 1 \
  --ports 8080 \
  --environment-variables SPRING_PROFILES_ACTIVE=production DATABASE_URL=your_db_url JWT_SECRET=your_secret
```

---

## 🗄️ Database Setup

### Option 1: Managed PostgreSQL (Recommended)

#### **Neon (Free PostgreSQL)**
```bash
# 1. Go to https://neon.tech
# 2. Create free account
# 3. Create database
# 4. Copy connection string
DATABASE_URL=postgresql://username:password@ep-hostname.neon.tech/neondb
```

#### **Supabase (Free PostgreSQL)**
```bash
# 1. Go to https://supabase.com
# 2. Create project
# 3. Go to Settings → Database
# 4. Copy connection string
DATABASE_URL=postgresql://postgres:password@db.project.supabase.co:5432/postgres
```

#### **AWS RDS PostgreSQL**
```bash
# Create RDS instance
aws rds create-db-instance \
  --db-instance-identifier placement-db \
  --db-instance-class db.t3.micro \
  --engine postgres \
  --master-username placement_user \
  --master-user-password your_password \
  --allocated-storage 20
```

### Option 2: Docker Compose (Self-hosted)

```yaml
# docker-compose.prod.yml
version: '3.8'
services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: placement_expo
      POSTGRES_USER: placement_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  app:
    image: yourusername/placement-expo:latest
    environment:
      SPRING_PROFILES_ACTIVE: production
      DATABASE_URL: jdbc:postgresql://postgres:5432/placement_expo
      DATABASE_USERNAME: placement_user
      DATABASE_PASSWORD: ${DB_PASSWORD}
      JWT_SECRET: ${JWT_SECRET}
    ports:
      - "8080:8080"
    depends_on:
      - postgres

volumes:
  postgres_data:
```

---

## ⚙️ Environment Variables Reference

### Required Variables:
```env
# Database
DATABASE_URL=jdbc:postgresql://host:port/database
DATABASE_USERNAME=username
DATABASE_PASSWORD=password

# Security
JWT_SECRET=your-256-bit-secret-key-here

# Appwrite Integration
APPWRITE_PROJECT_ID=6894d20300386b659e32
APPWRITE_ENDPOINT=https://nyc.cloud.appwrite.io/v1

# Application
SPRING_PROFILES_ACTIVE=production
UPLOAD_DIR=/app/uploads
CORS_ALLOWED_ORIGINS=https://yourdomain.com

# Optional
MAX_FILE_SIZE=10MB
LOG_LEVEL=INFO
```

### Generate Strong JWT Secret:
```bash
# Method 1: OpenSSL
openssl rand -base64 32

# Method 2: Node.js
node -e "console.log(require('crypto').randomBytes(32).toString('base64'))"

# Method 3: Python
python -c "import secrets; print(secrets.token_urlsafe(32))"
```

---

## 🔧 Pre-deployment Checklist

### 1. **Update Application Properties**
```properties
# src/main/resources/application-production.properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

logging.level.com.placement.expo=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

server.port=${PORT:8080}

# File upload
upload.dir=${UPLOAD_DIR:/app/uploads}
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# CORS
cors.allowed.origins=${CORS_ALLOWED_ORIGINS:http://localhost:8080}

# Appwrite
appwrite.project.id=${APPWRITE_PROJECT_ID}
appwrite.endpoint=${APPWRITE_ENDPOINT}

# JWT
jwt.secret=${JWT_SECRET}
```

### 2. **Update Frontend URLs**
```javascript
// js/appwrite-config.js - Update for production
const client = new Client()
    .setEndpoint('https://nyc.cloud.appwrite.io/v1') // Your Appwrite URL
    .setProject('6894d20300386b659e32'); // Your project ID

// Update API base URL if needed
const API_BASE_URL = window.location.origin; // Use relative URLs
```

### 3. **Add Health Check Endpoint**
```java
// Add to application.properties
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

### 4. **Create Dockerfile (if not exists)**
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/placement_expo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

### 5. **Add Maven Wrapper** (if not exists)
```bash
mvn wrapper:wrapper
```

---

## 🚦 Testing Deployment

### 1. **Local Testing**
```bash
# Test with production profile
export SPRING_PROFILES_ACTIVE=production
export DATABASE_URL=jdbc:h2:mem:testdb
export JWT_SECRET=test-secret-key-for-local-testing-only
mvn spring-boot:run

# Test endpoints
curl http://localhost:8081/actuator/health
curl http://localhost:8081/api/v1/ats/sample
```

### 2. **Docker Testing**
```bash
# Build and test container
docker build -t placement-expo:test .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=production \
  -e DATABASE_URL=jdbc:h2:mem:testdb \
  -e JWT_SECRET=test-secret \
  placement-expo:test

# Test container
curl http://localhost:8080/actuator/health
```

### 3. **Production Testing**
```bash
# After deployment, test these endpoints:
curl https://your-app.herokuapp.com/actuator/health
curl https://your-app.herokuapp.com/api/v1/ats/sample
curl https://your-app.herokuapp.com/
```

---

## 📊 Monitoring & Maintenance

### 1. **Set up Monitoring**
```bash
# Add application.properties
management.endpoints.web.exposure.include=health,metrics,info
management.metrics.export.prometheus.enabled=true
```

### 2. **Log Aggregation**
```bash
# Use platform-specific logging:
# Heroku: heroku logs --tail
# Railway: Check Railway dashboard
# DigitalOcean: Check App Platform logs
```

### 3. **Database Backups**
```bash
# Enable automated backups on your cloud provider
# Or set up manual backup script:
pg_dump $DATABASE_URL > backup-$(date +%Y%m%d).sql
```

---

## 🎯 Recommended Deployment

**For Learning/Portfolio:** Railway or Render (FREE)
**For Production:** Heroku, DigitalOcean, or AWS (PAID but reliable)

### Quick Start with Railway:
1. Push code to GitHub
2. Connect Railway to your GitHub repo  
3. Add environment variables
4. Deploy automatically
5. Access your app at: `https://yourapp.railway.app`

**Total Time: 10-15 minutes** ⚡

---

## 🆘 Troubleshooting

### Common Issues:

1. **Build Fails**
   ```bash
   # Ensure Maven wrapper exists
   chmod +x mvnw
   ./mvnw clean package -DskipTests
   ```

2. **Database Connection Error**
   ```bash
   # Check DATABASE_URL format
   postgresql://username:password@hostname:port/database
   ```

3. **File Upload Not Working**
   ```bash
   # Ensure upload directory is writable
   mkdir -p $UPLOAD_DIR
   chmod 755 $UPLOAD_DIR
   ```

4. **CORS Issues**
   ```bash
   # Update CORS_ALLOWED_ORIGINS
   export CORS_ALLOWED_ORIGINS=https://yourapp.railway.app
   ```

5. **Memory Issues**
   ```bash
   # Add JVM options
   java -Xms512m -Xmx1024m -jar app.jar
   ```

Need help? Check the deployment platform's documentation or create an issue on GitHub!
