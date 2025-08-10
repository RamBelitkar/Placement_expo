# PlacementSyncer Server Startup Guide with Cache Clearing

## Quick Start Commands (Copy-Paste Ready)

### 1. Clean and Start Server (Recommended)
```powershell
# Navigate to project directory
cd "d:\Placement_expo"

# Clean Maven cache and rebuild
mvn clean compile

# Start the server
mvn spring-boot:run
```

### 2. Complete Cache Clear and Restart
```powershell
# Navigate to project directory
cd "d:\Placement_expo"

# Stop any running Java processes
Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue

# Clean all Maven generated files
mvn clean

# Clear browser cache (manual step - see instructions below)

# Rebuild and start fresh
mvn compile spring-boot:run
```

## Detailed Instructions

### Step 1: Navigate to Project Directory
```powershell
cd "d:\Placement_expo"
```

### Step 2: Stop Running Server (if any)
```powershell
# Method 1: Stop all Java processes
Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue

# Method 2: If you know the specific PID
# taskkill /F /PID <process_id>

# Method 3: Use Ctrl+C in the terminal where server is running
```

### Step 3: Clear Maven Cache
```powershell
# Clean all compiled files and dependencies
mvn clean

# Optional: Clear specific directories manually
Remove-Item -Recurse -Force "target\" -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force ".mvn\" -ErrorAction SilentlyContinue
```

### Step 4: Rebuild Project
```powershell
# Compile source code
mvn compile

# Or compile and package (creates JAR)
mvn clean package -DskipTests
```

### Step 5: Start Server
```powershell
# Method 1: Using Maven (Development)
mvn spring-boot:run

# Method 2: Run JAR directly (Production-like)
java -jar target\placement-syncer-1.0-SNAPSHOT.jar

# Method 3: With specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Browser Cache Clearing

### For Chrome:
1. Press `Ctrl + Shift + Delete`
2. Select "Cached images and files"
3. Click "Clear data"

### For Firefox:
1. Press `Ctrl + Shift + Delete`
2. Select "Cache"
3. Click "Clear Now"

### Hard Refresh (Any Browser):
- `Ctrl + F5` or `Ctrl + Shift + R`

## Server Access Information

### URLs:
- **Main Application**: http://localhost:8080
- **Dashboard**: http://localhost:8080/dashboard.html
- **H2 Database Console**: http://localhost:8080/h2-console
- **Actuator Health**: http://localhost:8080/actuator/health

### Default Credentials:
- **H2 Database**:
  - URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (leave empty)

## Troubleshooting

### Common Issues and Solutions:

#### 1. Port 8080 Already in Use
```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /F /PID <PID>
```

#### 2. Maven Build Failures
```powershell
# Clean everything and retry
mvn clean
mvn dependency:purge-local-repository
mvn compile
```

#### 3. Dashboard Not Updating
```powershell
# Force clean static resources
Remove-Item "target\classes\static\*" -Recurse -Force -ErrorAction SilentlyContinue
mvn clean compile
```

#### 4. Java Version Issues
```powershell
# Check Java version
java -version

# Should show Java 17.x.x
# If not, ensure JAVA_HOME is set correctly
echo $env:JAVA_HOME
```

### 5. Complete Reset (Nuclear Option)
```powershell
# Stop all Java processes
Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue

# Clean everything
mvn clean
Remove-Item -Recurse -Force "target\" -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force ".mvn\" -ErrorAction SilentlyContinue

# Clear Maven local repository cache for this project
mvn dependency:purge-local-repository

# Rebuild from scratch
mvn clean compile spring-boot:run
```

## Development Workflow

### For Daily Development:
```powershell
# Morning startup
cd "d:\Placement_expo"
mvn clean compile spring-boot:run
```

### After Making Changes:
```powershell
# Quick restart (if server is running)
# Press Ctrl+C to stop
mvn compile spring-boot:run
```

### After Major Changes:
```powershell
# Full clean restart
mvn clean compile spring-boot:run
```

## Server Status Monitoring

### Check if Server is Running:
```powershell
# Check Java processes
Get-Process java -ErrorAction SilentlyContinue

# Check port 8080
netstat -ano | findstr :8080

# Test HTTP endpoint
curl http://localhost:8080/actuator/health
```

### Server Logs:
- Watch the terminal where you ran `mvn spring-boot:run`
- Look for "Started Application in X.XXX seconds"
- Dashboard access logs will show when users visit the page

## Important Notes:

1. **Always use `mvn clean`** when you modify static files (HTML, CSS, JS)
2. **Clear browser cache** after updating dashboard files
3. **Server starts on port 8080** by default
4. **H2 database** is in-memory and resets on server restart
5. **Use Ctrl+C** to gracefully stop the server
6. **Dashboard path**: `/dashboard.html` (not just `/dashboard`)

## Success Indicators:

✅ Server starts without errors
✅ Can access http://localhost:8080
✅ Dashboard loads at http://localhost:8080/dashboard.html
✅ No "404 Not Found" errors
✅ Profile information displays correctly
✅ Browser console shows no JavaScript errors

## Emergency Commands:

```powershell
# Kill all Java processes immediately
Stop-Process -Name "java" -Force

# Kill specific process using port 8080
$port8080 = netstat -ano | findstr :8080 | ForEach-Object { ($_ -split '\s+')[4] }
if ($port8080) { taskkill /F /PID $port8080 }

# Complete project reset
cd "d:\Placement_expo"
mvn clean
mvn compile spring-boot:run
```

Save this guide and use these commands whenever you need to start the server or clear caches!
