import datetime
import os
import time

import cv2
import numpy as np
import face_recognition


path = 'ImagesAttendance'
images = []
classNames = []
myList = os.listdir(path)
for cl in myList:
    curImg = cv2.imread(f"{path}/{cl}")
    images.append(curImg)
    classNames.append(os.path.splitext(cl)[0])
# print(classNames)

def findEncodings(images):
    encodeList = []
    for img in images:
        img = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)
        encode = face_recognition.face_encodings(img)[0]
        encodeList.append(encode)
    return encodeList

def markAttendance(name):
    # time
    now = datetime.datetime.now()
    dateString = now.strftime('%d/%m/%Y')
    timeString = now.strftime('%H:%M:%S')
    # read file
    with open("AttendanceFile.csv", "r+") as f:
        myDataList = f.readlines()
        nameListToday = []
        for line in myDataList:
            entry = line.strip().split(',')
            if len(entry) > 2 and entry[2] == dateString:
                nameListToday.append(entry[0])
        # print("today's list", nameListToday)
        if name not in nameListToday:
            f.writelines('\n')
            f.writelines(f'{name},{timeString},{dateString}')

encodeListKnown = findEncodings(images)
# print('Encoding Complete')

cap = cv2.VideoCapture(0)
terminate = False

while True:
    # get image
    success, img = cap.read()
    imgS = cv2.resize(img, (0, 0), None, 0.25, 0.25)
    imgS = cv2.cvtColor(imgS, cv2.COLOR_RGB2BGR)
    # encode
    facesCurFrame = face_recognition.face_locations(imgS)
    encodesCurFrame = face_recognition.face_encodings(imgS, facesCurFrame)
    # find matches
    for encodeFace, faceLoc in zip(encodesCurFrame, facesCurFrame):
        matches = face_recognition.compare_faces(encodeListKnown, encodeFace, tolerance=0.5)
        faceDis = face_recognition.face_distance(encodeListKnown, encodeFace)
#         print(faceDis)
        matchIndex = np.argmin(faceDis)
        if matches[matchIndex]:
            name = classNames[matchIndex].upper()
            print(name)
            cv2.imwrite('images/c1.png',img)
            y1, x2, y2, x1 = faceLoc
            y1, x2, y2, x1 = y1*4, x2*4, y2*4, x1*4
            cv2.rectangle(img, (x1, y1), (x2, y2), (0, 255, 0), 2)
            cv2.rectangle(img, (x1, y2-35), (x2, y2), cv2.FILLED)
            cv2.putText(img, name, (x1+6, y2-6), cv2.FONT_HERSHEY_COMPLEX, 1, (255, 255, 255), 2)
            markAttendance(name)
    #         stop the running
            terminate = True

    # show cam
    cv2.imshow("WebCam", img)
    if cv2.waitKey(1) == ord('q') or terminate:
        # time.sleep(5)
        cap.release()
        cv2.destroyAllWindows()
        break

