import requests
import json
import time
import random


def main():

    url = 'http://192.168.0.114:8080/order/SendOrder'
    ip = '192.168.0.114'

    headers = {"content-type": "application/json", "Authorization": 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJFSVMiLCJzdWIiOiJ0ZXN0IiwiZXhwIjoxNTYwMT'
                                                                    + 'gwMjkxLCJpYXQiOjE1NjAxNzY2OTE4Mzh9'
                                                                    + '.dzjrA9enqe1yHv57D4Xj5AP57fJVO1mUkm4OgXSB56QOg'
                                                                    + 'gGagNCLknDm3QRz50W7cNHIyNBAVU9N3ArGxCN_lw'}
    for i in range(50):
        price = round(random.uniform(5, 8), 1)
        amount = random.randint(100, 150)
        ways = ['S', 'B']
        way = ways[random.randint(0, 1)]
        types = ['M', 'L', 'S']
        type = types[random.randint(0, 2)]
        body = {'futureID': 'U', 'amount': amount, 'way': way, 'price': price, 'type': type, 'brokerIp': ip}
        response = requests.post(url, json=body, headers=headers)
        time.sleep(0.5)


if __name__ == '__main__':
    main()
