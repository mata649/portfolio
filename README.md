# 🌐 Personal Portfolio Website

This repository contains the source code and infrastructure configuration for my personal portfolio website.  
The application is designed to showcase **projects, skills, and professional experience**, while also serving as a practical demonstration of **modern DevOps and cloud practices**.

---

## 📖 Project Overview

The portfolio website is built with **Django** and runs on **PostgreSQL** as the primary database.  
The entire application is **containerized with Docker and Docker Compose**, allowing it to be easily replicated and deployed locally or in the cloud.

For production, the project is deployed on **AWS** using **Terraform** as Infrastructure as Code (IaC).  
A **CI/CD pipeline with GitHub Actions** ensures continuous delivery, automatically building, testing, and deploying the application whenever changes are pushed.

The cloud infrastructure is composed of several AWS services working together:

- **EC2** → Virtual machine hosting the Django application in containers.  
- **RDS (PostgreSQL)** → Managed relational database for storing project, skill, and experience data.  
- **S3** → Storage for static and media files.  
- **CloudFront** → Global CDN and entry point for the website and assets.  
- **Route 53** → DNS management to route traffic to the application.  

This setup highlights the integration of **Python/Django development** with **DevOps practices**, **cloud services**, and **infrastructure automation**.

---

## 🛠️ Tech Stack

- **Programming Language:** Python  
- **Framework:** Django  
- **Database:** PostgreSQL (AWS RDS)  
- **Containerization:** Docker, Docker Compose  
- **Infrastructure:** Terraform (IaC)  
- **Cloud Provider:** AWS (EC2, RDS, S3, CloudFront, Route 53)  
- **CI/CD:** GitHub Actions  

---
