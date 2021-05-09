import io
import os
import time

import cv2
import numpy as np
from PIL import Image
from flask import Flask, request, jsonify, send_file, make_response, send_from_directory

app = Flask(__name__)



def test(im1):
    Grayimg = cv2.cvtColor(im1, cv2.COLOR_BGR2GRAY)  # 先要转换为灰度图片
    ret, thresh = cv2.threshold(Grayimg, 150, 255, cv2.THRESH_BINARY)  # 这里的第二个参数要调，是阈值！！
    return ret ,thresh

# 文件上传
@app.route('/upload', methods=['POST'])
def v_upload():
    try:
        upload_file = request.files['file'].read()
        im1 = cv2.imdecode(np.frombuffer(upload_file, np.uint8), cv2.IMREAD_COLOR)
        # print(type(upload_file))
        # upload_file = upload_file.read()
        # print(type(upload_file))
        # img_stream = io.BytesIO(upload_file)
        # print(type(img_stream))
        # img = Image.open(img_stream)
        # im2 = cv2.imread(img)
        ret,thresh = test(im1)
        im = Image.fromarray(thresh)
        filename = str(int(time.time()))
        cv2.imwrite("./templates/"+filename+'.png', thresh)
        return jsonify({"state": "success", "filename":filename+'.png',"ret":ret})
        # imgByteArr = io.BytesIO()
        # im.save(imgByteArr, format='JPEG')
        # imgByteArr = imgByteArr.getvalue()
        # return send_file(
        #     io.BytesIO(imgByteArr),
        #     mimetype='image/png',
        #     as_attachment=True,
        #     attachment_filename='result.jpg'
        # )
    except Exception as e:
        print(e)
        return {"state": "error"}


@app.route('/download', methods=['GET'])
def v_download():
    """
    文件下载
    :return:
    """
    fileName = request.args.get("fileName")
    # 设置目录
    FileSaveDir = "templates"
    # 文件夹拼接
    filePath = os.path.join(FileSaveDir)
    print(filePath)
    try:
        response = make_response(send_from_directory(filePath, fileName, as_attachment=True))
        return response
    except Exception as e:
        return jsonify({"code": "异常", "message": "{}".format(e)})

if __name__ == '__main__':
    app.run()
