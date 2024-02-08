# Rate limiter 

### Overview

### Project setup 

### REST APIs

### Postman configurations
Load profile = Fixed \
Virtual users = 10 \
Test duration = 1 min

### Token Bucket Algorithm
Refill rate = 1 \
Bucket size = 10
![img.png](src/main/resources/images/tba_1_10.png)

Refill rate = 10 \
Bucket size = 10
![img.png](src/main/resources/images/tba_10_10.png)

### Fixed Counter Window Algorithm
Window size = 20 \
Bucket size = 10
![img.png](src/main/resources/images/fcwa_20_10.png)

Window size = 60 \
Bucket size = 10
![img.png](src/main/resources/images/fcwa_60_10.png)

### Sliding Counter Window Algorithm
Window size = 20 \
Bucket size = 10 
![img_1.png](src/main/resources/images/scwa_20_10.png)

Window size = 60 \
Bucket size = 10 
![img.png](src/main/resources/images/scwa_60_10.png)
