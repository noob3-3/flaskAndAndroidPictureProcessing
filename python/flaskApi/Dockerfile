FROM python:3.6
WORKDIR /root/python

COPY requirements.txt ./
RUN pip install -r requirementsfordocker.txt -i https://pypi.tuna.tsinghua.edu.cn/simple

COPY .. .

CMD ["gunicorn", "app:app", "-c", "./gunicorn.conf.py"]
