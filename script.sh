#!/bin/bash
for i in {1..20}; do
  curl -s -X POST "http://localhost:8083/orders?userId=1&productId=3&quantity=1" &
done
wait