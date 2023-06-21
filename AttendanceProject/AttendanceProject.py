import datetime
import os
import time
import re
import cv2
import numpy as np
import face_recognition


# path = 'ImagesAttendance'
# images = []
# classNames = []
# myList = os.listdir(path)
# for cl in myList:
#     curImg = cv2.imread(f"{path}/{cl}")
#     images.append(curImg)
#     classNames.append(os.path.splitext(cl)[0])
# print(classNames)


def ImageFilesInFolder(folder):
    #following code snippet from face_recognition
    return [os.path.join(folder, f) for f in os.listdir(folder) if re.match(r'.*\.(jpg|jpeg|png)', f, flags=re.I)]



def ScanKnownPeople(known_people_folder):
    names = []
    face_encodings = []

    for file in ImageFilesInFolder(known_people_folder):
        filename = os.path.splitext(os.path.basename(file))[0]
        # print("DEBUG: Attempted to load image file from", file)
        image = face_recognition.load_image_file(file)

        if os.path.isfile(os.path.join(known_people_folder, "PreEncoded", filename) + ".npy"):
            # read from file, for performance reasons. useful for large batches of files
            encodedfile = np.load((os.path.join(known_people_folder, "PreEncoded", filename) + ".npy"))

            if encodedfile is not None:
                names.append(filename)
                # print("DEBUG: appended from document", filename)
                face_encodings.append(encodedfile)
                # print("DEBUG: appended from document", encodedfile)
        else:
            single_encoding = face_recognition.face_encodings(image)

            # if len(single_encoding) > 1:
                # print("WARNING: More than one face found in", file + ".", "Only using the first face.")

            # if len(single_encoding) == 0:
                # print("WARNING: No faces found in", file + ".", "Ignoring file.")
            # else:
            names.append(filename)
            face_encodings.append(single_encoding[0])

            # write to file, for performance reasons, so as to not calculate all the faces each time
            encodedfile = np.save((os.path.join(known_people_folder, "PreEncoded", filename) + ".npy"),
                                     single_encoding[0])
            # print("DEBUG: saved to document", filename)
            # print("DEBUG: saved to document", encodedfile)

    return names, face_encodings


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


# encodeListKnown = findEncodings(images)
# print('Encoding Complete')
names, encodeListKnown = ScanKnownPeople("ImagesAttendance")

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
            name = names[matchIndex].upper()
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

