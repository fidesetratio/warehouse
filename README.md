# Shop Warehouse Management API

RESTful API sederhana untuk mengelola inventory gudang sebuah toko. Project ini dibuat untuk memenuhi **Java Backend Developer Technical Assessment**.

## Teknologi

- Java 17
- Spring Boot 3.5.16
- Spring Web
- Spring Data JPA / Hibernate
- H2 Database
- Jakarta Bean Validation
- Maven

## Arsitektur

Aplikasi menggunakan layered architecture sederhana:

```text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
H2 Database
```

Struktur domain:

```text
Item 1 ---- * ItemVariant
```

`Item` merepresentasikan produk, sedangkan `ItemVariant` merepresentasikan variasi produk yang dapat dijual, misalnya ukuran, warna, atau konfigurasi tertentu.

Harga dan stock disimpan pada level `ItemVariant` karena variant merupakan unit yang benar-benar dijual.

---

## Design Decisions

### 1. Layered Architecture

Aplikasi memisahkan tanggung jawab menjadi beberapa layer:

- **Controller** — menangani HTTP request dan response.
- **Service** — menangani business logic.
- **Repository** — menangani akses database.
- **Entity** — merepresentasikan data database.
- **DTO** — digunakan untuk request dan response API.
- **Exception** — menangani error aplikasi.

Pemisahan ini membuat business logic tidak bergantung langsung pada REST API.

### 2. Stock Disimpan pada Variant

Stock disimpan pada `ItemVariant`, bukan pada `Item`.

Contohnya:

```text
T-Shirt
 ├── Red / M     Stock: 20
 └── Blue / L    Stock: 15
```

Setiap variant dapat memiliki jumlah stock yang berbeda.

### 3. Penjualan sebagai Business Operation

Penjualan inventory tidak dilakukan melalui update stock biasa.

Digunakan endpoint:

```text
POST /api/v1/variants/{id}/sell
```

Service akan memeriksa stock terlebih dahulu sebelum melakukan pengurangan.

Jika jumlah yang dijual lebih besar daripada stock yang tersedia, transaksi ditolak dengan HTTP `409 Conflict`.

Stock tidak akan menjadi negatif.

### 4. Penggunaan H2

H2 dipilih karena assessment mengizinkan penggunaan in-memory database.

Keuntungannya adalah aplikasi dapat langsung dijalankan tanpa membutuhkan instalasi database eksternal seperti MySQL atau PostgreSQL.

### 5. Database Constraint dan Index

`SKU` dibuat unique karena setiap SKU harus mengidentifikasi satu variant yang berbeda.

Index pada `item_id` digunakan karena variant sering dicari berdasarkan parent item.

### 6. Validation

Bean Validation digunakan untuk memastikan request yang masuk valid.

Contoh:

- Nama item tidak boleh kosong.
- SKU tidak boleh kosong.
- Harga tidak boleh negatif.
- Stock tidak boleh negatif.
- Quantity penjualan harus lebih besar dari 0.

---

## Asumsi

1. Setiap SKU mengidentifikasi satu variant.
2. Satu item dapat memiliki banyak variant.
3. Harga dan stock disimpan pada level variant.
4. Penjualan melebihi stock yang tersedia tidak diperbolehkan.
5. Currency conversion tidak termasuk dalam scope assessment.
6. Authentication dan authorization tidak termasuk dalam scope assessment.
7. H2 in-memory database sudah cukup karena assessment tidak membutuhkan database eksternal.
8. Sample data dibuat otomatis ketika aplikasi dijalankan.

---

## Cara Menjalankan Aplikasi

### Requirement

Pastikan sudah tersedia:

- Java 17 atau lebih baru
- Maven 3.9 atau lebih baru

### Menjalankan Test

```bash
mvn clean test
```

### Menjalankan Application

```bash
mvn spring-boot:run
```

Application berjalan pada:

```text
http://localhost:8080
```

---

## Sample Data

Ketika aplikasi dijalankan, beberapa sample data akan dibuat secara otomatis.

### T-Shirt

| SKU | Variant | Harga | Stock |
|---|---|---:|---:|
| TSHIRT-RED-M | Red / M | 100000 | 20 |
| TSHIRT-BLUE-L | Blue / L | 110000 | 15 |

### Laptop

| SKU | Variant | Harga | Stock |
|---|---|---:|---:|
| LAPTOP-8GB | 8GB RAM | 7500000 | 10 |
| LAPTOP-16GB | 16GB RAM | 9000000 | 5 |

---

## API Endpoint

### Item

```text
GET    /api/v1/items
GET    /api/v1/items/{id}
POST   /api/v1/items
PUT    /api/v1/items/{id}
DELETE /api/v1/items/{id}
```

### Variant

```text
GET    /api/v1/items/{itemId}/variants
POST   /api/v1/items/{itemId}/variants
PUT    /api/v1/variants/{id}
DELETE /api/v1/variants/{id}
```

### Stock

```text
POST /api/v1/variants/{id}/sell
POST /api/v1/variants/{id}/stock
```

---

## Contoh API

### 1. Membuat Item

```http
POST /api/v1/items
Content-Type: application/json
```

Request:

```json
{
  "name": "T-Shirt",
  "description": "Cotton T-Shirt"
}
```

### 2. Membuat Variant

```http
POST /api/v1/items/1/variants
Content-Type: application/json
```

Request:

```json
{
  "sku": "TSHIRT-BLACK-L",
  "name": "Black / L",
  "price": 120000,
  "stock": 10
}
```

### 3. Menjual Stock

```http
POST /api/v1/variants/1/sell
Content-Type: application/json
```

Request:

```json
{
  "quantity": 3
}
```

Jika sebelumnya stock adalah `10`, maka stock menjadi:

```text
7
```

### 4. Menjual Melebihi Stock

Request:

```http
POST /api/v1/variants/1/sell
Content-Type: application/json
```

```json
{
  "quantity": 100
}
```

Response:

```http
409 Conflict
```

```json
{
  "code": "INSUFFICIENT_STOCK",
  "message": "Insufficient stock. Available: 7"
}
```

Stock tetap `7` dan tidak menjadi negatif.

---

## H2 Console

H2 Console dapat diakses melalui:

```text
http://localhost:8080/h2-console
```

Connection:

```text
JDBC URL: jdbc:h2:mem:warehouse
Username: sa
Password:
```

---

## Testing

Test mencakup business logic utama untuk inventory, khususnya:

- Berhasil mengurangi stock ketika quantity mencukupi.
- Menolak penjualan ketika stock tidak mencukupi.
- Stock tetap tidak berubah ketika penjualan ditolak.

---

## Git Commit History

Commit dibuat berdasarkan perubahan fitur agar history repository mudah dibaca.

Contoh:

```text
chore: initialize warehouse assessment
feat: add item and variant domain
feat: add item and variant CRUD APIs
feat: add stock management
feat: add validation and exception handling
test: add stock business tests
docs: finalize README
```

---

## Kesimpulan

Project ini dibuat dengan fokus pada requirement utama assessment:

- RESTful API
- CRUD Item dan Variant
- Price management
- Stock management
- Pencegahan penjualan melebihi stock
- Validation
- Database persistence
- Unit testing
- Dokumentasi dan sample data