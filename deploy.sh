#!/bin/bash

# AIUEducation Deployment Script for freelance.com.kz
set -e

echo "🚀 Starting deployment of AIUEducation..."

# Configuration
APP_NAME="aiueducation"
DOMAIN="freelance.com.kz"
DB_PASSWORD=${DB_PASSWORD:-$(openssl rand -base64 32)}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Functions
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is installed
check_docker() {
    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed. Please install Docker first."
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose is not installed. Please install Docker Compose first."
        exit 1
    fi
    
    log_info "Docker and Docker Compose are installed ✓"
}

# Create necessary directories
create_directories() {
    log_info "Creating necessary directories..."
    mkdir -p logs
    mkdir -p backups
    log_info "Directories created ✓"
}

# Create environment file
create_env_file() {
    log_info "Creating environment file..."
    cat > .env << EOF
# Database configuration
DB_PASSWORD=${DB_PASSWORD}

# Application configuration
SPRING_PROFILES_ACTIVE=prod
DOMAIN=${DOMAIN}

# Security
JWT_SECRET=$(openssl rand -base64 64)
EOF
    log_info "Environment file created ✓"
}

# Create database initialization script
create_init_sql() {
    log_info "Creating database initialization script..."
    cat > init.sql << 'EOF'
-- Create database if not exists
CREATE DATABASE IF NOT EXISTS aiueducation_prod;

-- Create user if not exists
DO
$do$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_catalog.pg_roles
      WHERE  rolname = 'aiueducation') THEN
      
      CREATE ROLE aiueducation LOGIN PASSWORD 'your_secure_password';
   END IF;
END
$do$;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE aiueducation_prod TO aiueducation;
EOF
    log_info "Database initialization script created ✓"
}

# Build and start services
deploy_services() {
    log_info "Building and starting services..."
    
    # Stop existing services
    docker-compose -f docker-compose.prod.yml down 2>/dev/null || true
    
    # Build and start
    docker-compose -f docker-compose.prod.yml up --build -d
    
    log_info "Services started ✓"
}

# Wait for services to be healthy
wait_for_services() {
    log_info "Waiting for services to be healthy..."
    
    # Wait for database
    log_info "Waiting for database..."
    timeout 60 bash -c 'until docker-compose -f docker-compose.prod.yml exec -T db pg_isready -U aiueducation -d aiueducation_prod; do sleep 2; done'
    
    # Wait for application
    log_info "Waiting for application..."
    timeout 120 bash -c 'until curl -f http://localhost/health; do sleep 5; done'
    
    log_info "All services are healthy ✓"
}

# Show deployment status
show_status() {
    log_info "Deployment Status:"
    echo "===================="
    docker-compose -f docker-compose.prod.yml ps
    echo ""
    log_info "Application URL: http://${DOMAIN}"
    log_info "Health Check: http://${DOMAIN}/health"
    log_info "Database Password: ${DB_PASSWORD}"
    echo ""
    log_warn "Please save the database password in a secure location!"
}

# Backup function
backup_data() {
    log_info "Creating backup..."
    BACKUP_FILE="backups/backup_$(date +%Y%m%d_%H%M%S).sql"
    docker-compose -f docker-compose.prod.yml exec -T db pg_dump -U aiueducation aiueducation_prod > ${BACKUP_FILE}
    log_info "Backup created: ${BACKUP_FILE} ✓"
}

# Main deployment process
main() {
    log_info "Starting deployment for ${DOMAIN}..."
    
    check_docker
    create_directories
    create_env_file
    create_init_sql
    
    # Create backup if services are running
    if docker-compose -f docker-compose.prod.yml ps | grep -q "Up"; then
        backup_data
    fi
    
    deploy_services
    wait_for_services
    show_status
    
    log_info "🎉 Deployment completed successfully!"
    log_info "Your AIUEducation platform is now running at http://${DOMAIN}"
}

# Handle script arguments
case "${1:-deploy}" in
    "deploy")
        main
        ;;
    "backup")
        backup_data
        ;;
    "status")
        show_status
        ;;
    "logs")
        docker-compose -f docker-compose.prod.yml logs -f
        ;;
    "stop")
        docker-compose -f docker-compose.prod.yml down
        log_info "Services stopped ✓"
        ;;
    "restart")
        docker-compose -f docker-compose.prod.yml restart
        log_info "Services restarted ✓"
        ;;
    *)
        echo "Usage: $0 {deploy|backup|status|logs|stop|restart}"
        exit 1
        ;;
esac 