# Spring WebFlux Reactive Uygulaması

Bu proje, Spring WebFlux kullanılarak reaktif programlama prensipleriyle geliştirilmiş bir örnek uygulamadır. Amaç,
öğrenci ve kurs bilgilerini veritabanından asenkron şekilde çekmek ve RESTful API üzerinden sunmaktır. Uygulamada
PostgreSQL veritabanı ve R2DBC (Reactive Relational Database Connectivity) kullanılmıştır. Ayrıca, JSON dönüşümleri için
özel converter’lar ile kurs meta verileri (metadata) yönetilmektedir.

## İçindekiler

- [Özellikler](#özellikler)
- [Kullanılan Teknolojiler](#kullanılan-teknolojiler)
- [Proje Yapısı](#proje-yapısı)
- [Veritabanı Yapısı ve Konfigürasyonu](#veritabanı-yapısı-ve-konfigürasyonu)
- [Docker Konfigürasyonu](#docker-konfigürasyonu)
- [API Dokümantasyonu](#api-dokümantasyonu)
- [Kurulum ve Çalıştırma Talimatları](#kurulum-ve-çalıştırma-talimatları)
- [Geliştirme Önerileri](#geliştirme-önerileri)

---

## Özellikler

- **Reaktif Programlama:** Uygulama, Spring WebFlux çerçevesi ile reaktif (asenkron) mimari prensiplerine uygun olarak
  geliştirilmiştir.
- **Veritabanı İşlemleri:** PostgreSQL ve R2DBC kullanılarak, veritabanı ile reaktif bir şekilde bağlantı
  sağlanmaktadır.
- **Özel JSON Dönüşümleri:** `CourseMetadata` bilgisi için JSON–Object dönüşümleri, özel yazılan
  `CourseMetadataToJsonConverter` ve `JsonToCourseMetadataConverter` ile gerçekleştirilir.
- **REST API:** Öğrenci ve kurs bilgilerini sunan API endpoint’leri mevcuttur.
- **Fonksiyonel Routing:** WebFlux’ın fonksiyonel routing özelliğini kullanarak, API endpoint’leri için alternatif yapı
  sağlanmıştır.

---

## Kullanılan Teknolojiler

- **Java 21:** Uygulamanın geliştirilmesinde kullanılan ana programlama dili.
- **Spring Boot / Spring WebFlux:** Reaktif uygulama geliştirme için kullanılan çerçeve.
- **R2DBC:** Reaktif veri erişimi için PostgreSQL kullanılarak yapılandırılmıştır.
- **PostgreSQL:** İlişkisel veritabanı yönetim sistemi.
- **Docker:** Uygulamanın veritabanı kısmını containerize etmek için Docker Compose kullanılmıştır.
- **Lombok:** Kodda tekrarı önlemek, getter/setter gibi işlemleri otomatikleştirmek için kullanılmıştır.
- **Jackson:** JSON dönüşümleri ve serileştirme/deserileştirme işlemleri için kullanılır.

---

## Proje Yapısı

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── org.pehlivanmert.springwebflux
│   │   │       ├── config
│   │   │       │    ├── DatabaseConfig.java
│   │   │       │    └── converter
│   │   │       │         ├── CourseMetadataToJsonConverter.java
│   │   │       │         └── JsonToCourseMetadataConverter.java
│   │   │       ├── controller
│   │   │       │    ├── StudentController.java
│   │   │       │    └── CourseController.java
│   │   │       ├── dto
│   │   │       │    ├── CourseDto.java
│   │   │       │    ├── StudentDto.java
│   │   │       │    └── StudentListDto.java
│   │   │       ├── modal
│   │   │       │    ├── Student.java
│   │   │       │    ├── Course.java
│   │   │       │    └── metadata
│   │   │       │           ├── CourseMetadata.java
│   │   │       │           ├── CourseConstant.java
│   │   │       │           ├── SpringCourseMetadata.java
│   │   │       │           └── EnglishCourseMetadata.java
│   │   │       ├── repository
│   │   │       │    ├── StudentRepository.java
│   │   │       │    └── CourseRepository.java
│   │   │       ├── router
│   │   │       │    ├── StudentHandler.java
│   │   │       │    └── StudentRouter.java
│   │   │       └── service
│   │   │            ├── StudentService.java
│   │   │            └── CourseService.java
│   │   └── resources
│   │        ├── application.yml
│   │        └── db
│   │             └── migration
│   │                  ├── V1__create_course_table.sql
│   │                  └── V2__create_student_table.sql
└── docker-compose.yml
```

### Açıklamalar

- **Controller Katmanı:** REST API endpoint’lerini içermektedir. Örneğin, `StudentController` ve `CourseController`
  sınıfları ilgili servisleri çağırarak verileri sunar.
- **Service Katmanı:** İş mantığının yazıldığı katmandır. Öğrenci ve kurs verileri için ilgili servisler, repository’ler
  üzerinden veritabanı işlemlerini gerçekleştirir.
- **Repository Katmanı:** Spring Data R2DBC kullanılarak, veritabanı işlemleri gerçekleştirilmektedir.
- **Configuration:** `DatabaseConfig` dosyasında veritabanı bağlantısı ve R2DBC konfigürasyon ayarları yer almaktadır.
- **Converter:** JSON ile `CourseMetadata` arasında dönüşümü sağlayan iki özel converter (
  `CourseMetadataToJsonConverter` ve `JsonToCourseMetadataConverter`) bulunmaktadır.
- **Router:** Fonksiyonel routing ile API endpoint’lerinin alternatif şekilde tanımlanmasını sağlar (`StudentRouter`,
  `StudentHandler`).

---

## Veritabanı Yapısı ve Konfigürasyonu

Uygulama PostgreSQL veritabanı kullanılarak çalışmaktadır. Aşağıdaki SQL sorguları, ilgili tabloların (course ve
student) oluşturulmasını sağlamaktadır:

### Course Tablosu

```sql
CREATE TABLE course
(
    course_id       uuid         NOT NULL,
    name            varchar(255) NOT NULL,
    description     varchar(255) NOT NULL,
    duration        int4         NOT NULL,
    teacher         varchar(255) NOT NULL,
    course_metadata jsonb        NOT NULL,
    PRIMARY KEY (course_id)
);
```

### Student Tablosu

```sql
CREATE TABLE student
(
    id            uuid         NOT NULL,
    name          varchar(255) NOT NULL,
    date_of_birth date         NOT NULL,
    email         varchar(255) NOT NULL,
    course_id     VARCHAR[]    NOT NULL,
    PRIMARY KEY (id)
);
```

**Not:** Öğrencilerin kurs bilgileri, `student` tablosunda bir dizi (array) olarak saklanmaktadır. Her bir kurs için,
`UUID` tipindeki ID değeri String tipinde tutulmakta ve daha sonra servis katmanında ilgili `Course` bilgileri
çekilmektedir.

---

## Docker Konfigürasyonu

Uygulama ile birlikte çalışacak PostgreSQL veritabanı container’ı için aşağıdaki `docker-compose.yml` dosyası
kullanılabilir:

```yaml
version: '3.8'

services:
  spring-webflux-db:
    image: postgres:13.1-alpine
    container_name: spring-webflux-db
    ports:
      - 5435:5432
    restart: always
    environment:
      - POSTGRES_USER=userdb
      - POSTGRES_PASSWORD=pwduserdb
      - POSTGRES_DB=webfluxdb
```

Bu yapılandırma, PostgreSQL’in alpine sürümünü kullanarak, 5435 portundan erişime açılmasını sağlar.  
Veritabanı bağlantı ayarları `application.yml` dosyasında da aynen belirtilmiştir.

---

## API Dokümantasyonu

Uygulamada iki ana REST API endpoint’i bulunmaktadır:

1. **Student API**
    - **GET /v1/student:** Tüm öğrencileri ve ilgili detay bilgilerini döner.
    - **GET /v1/student/courses:** Öğrenciler ve onların kurs bilgilerini detaylı olarak dönen endpoint. Fonksiyonel
      routing yapılandırması ile tanımlanmıştır. Swagger/OpenAPI anotasyonları kullanılmıştır.

2. **Course API**
    - **GET /v1/course:** Tüm kurs bilgilerini döner.

Postman ile , API endpoint’lerini, test edebilir ve etkileşimde bulunabilirsiniz.

---

## Kurulum ve Çalıştırma Talimatları

1. **Klonlama ve Yapılandırma:**

   ```bash
   git clone https://github.com/PehlivanMert/Spring-webflux.git
   cd Spring-webflux
   ```

2. **Veritabanı Container’ını Başlatma:**

   Docker Compose ile veritabanı container’ını başlatmak için:

   ```bash
   docker-compose up --build
   ```

3. **Projeyi Derleme ve Çalıştırma:**

   Maven için:

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

4. **API Testleri:**

    - Tarayıcı üzerinden veya Postman gibi
      araçlarla [http://localhost:8081/v1/student](http://localhost:8081/v1/student)
      ve [http://localhost:8081/v1/course](http://localhost:8081/v1/course) endpoint’lerine istek atabilirsiniz.

---

## Geliştirme Önerileri

- **Exception Handling:** Hataların merkezi bir şekilde ele alınabilmesi için global exception handler eklenebilir.
- **Validasyon:** API giriş verileri için validasyon mekanizmaları entegre edilebilir.
- **Testler:** Birim ve entegrasyon testleri ile uygulamanın test kapsamı genişletilebilir.
- **Logging ve Monitoring:** Uygulama performansı ve hata takibi için gelişmiş loglama mekanizmaları (ör. Spring Boot
  Actuator) entegre edilebilir.

---

Happy Coding!