import argparse
import os
import platform
import shutil
import time
from pathlib import Path
import numpy
import cv2
import torch
import torch.backends.cudnn as cudnn
from numpy import random
import sys
sys.path.insert(0,'./face_detection')

from face_detection.models.experimental import attempt_load
from face_detection.utils.datasets import LoadStreams, LoadImages
from face_detection.utils.general import (
    check_img_size, non_max_suppression, apply_classifier, scale_coords,
    xyxy2xywh, plot_one_box, strip_optimizer, set_logging)
from face_detection.utils.torch_utils import select_device, load_classifier, time_synchronized



def letterbox(img, new_shape=(640, 640), color=(114, 114, 114), auto=True, scaleFill=False, scaleup=True):
    # Resize image to a 32-pixel-multiple rectangle https://github.com/ultralytics/yolov3/issues/232
    shape = img.shape[:2]  # current shape [height, width]
    if isinstance(new_shape, int):
        new_shape = (new_shape, new_shape)

    # Scale ratio (new / old)
    r = min(new_shape[0] / shape[0], new_shape[1] / shape[1])
    if not scaleup:  # only scale down, do not scale up (for better test mAP)
        r = min(r, 1.0)

    # Compute padding
    ratio = r, r  # width, height ratios
    new_unpad = int(round(shape[1] * r)), int(round(shape[0] * r))
    dw, dh = new_shape[1] - new_unpad[0], new_shape[0] - new_unpad[1]  # wh padding
    if auto:  # minimum rectangle
        dw, dh = numpy.mod(dw, 64), numpy.mod(dh, 64)  # wh padding
    elif scaleFill:  # stretch
        dw, dh = 0.0, 0.0
        new_unpad = (new_shape[1], new_shape[0])
        ratio = new_shape[1] / shape[1], new_shape[0] / shape[0]  # width, height ratios

    dw /= 2  # divide padding into 2 sides
    dh /= 2

    if shape[::-1] != new_unpad:  # resize
        img = cv2.resize(img, new_unpad, interpolation=cv2.INTER_LINEAR)
    top, bottom = int(round(dh - 0.1)), int(round(dh + 0.1))
    left, right = int(round(dw - 0.1)), int(round(dw + 0.1))
    img = cv2.copyMakeBorder(img, top, bottom, left, right, cv2.BORDER_CONSTANT, value=color)  # add border
    return img, ratio, (dw, dh)


def detect(source):
    out, weights, imgsz = \
        'inference/images', 'face_detection/face_weight.pt', 640
    device = select_device('cpu')
    if os.path.exists(out):
        shutil.rmtree(out)  # delete output folder
    os.makedirs(out)  # make new output folder
    half = device.type != 'cpu'  # half precision only supported on CUDA

    # Load model
    model = attempt_load(weights, map_location=device)  # load FP32 model
    if half:
        model.half()  # to FP16

    names = model.module.names if hasattr(model, 'module') else model.names

    # Padded resize
    img = letterbox(source, new_shape=640)[0]

    # Convert
    img = img[:, :, ::-1].transpose(2, 0, 1)  # BGR to RGB, to 3x416x416
    img = numpy.ascontiguousarray(img)

    img = torch.from_numpy(img).to(device)
    img = img.half() if half else img.float()  # uint8 to fp16/32
    img /= 255.0  # 0 - 255 to 0.0 - 1.0
    if img.ndimension() == 3:
        img = img.unsqueeze(0)

    pred = model(img, augment=False)[0]

    # Apply NMS
    pred = non_max_suppression(pred, 0.4, 0.2, classes=False, agnostic=False)
    label =''
         # Process detections
    for i, det in enumerate(pred):  # detections per image
        s, im0 =  '', img

        gn = torch.tensor(im0.shape)[[1, 0, 1, 0]]  # normalization gain whwh
        if det is not None and len(det):
            # Rescale boxes from img_size to im0 size
            det[:, :4] = scale_coords(img.shape[2:], det[:, :4], im0.shape).round()


            # Write results
            for *xyxy, conf, cls in reversed(det):
                #label = '%s %.2f' % (names[int(cls)], conf)
                label = '%s' % names[int(cls)]
                #plot_one_box(xyxy, im0, label=label, color=(255,255,255), line_thickness=3)


        # Stream results
        if False:
            cv2.imshow('asd', source)
            if cv2.waitKey(1) == ord('q'):  # q to quit
                raise StopIteration

    return label
