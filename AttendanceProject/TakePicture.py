import sys
import cv2

def main():
    # initialize the camera
    cam = cv2.VideoCapture(0)   # 0 -> index of camera
    s, img = cam.read()
    if s:    # frame captured without any errors
        cv2.namedWindow("cam-test", cv2.WINDOW_NORMAL)
        cv2.imshow("cam-test",img)
        cv2.waitKey(0)
        cv2.destroyWindow("cam-test")
        cv2.imwrite(f"ImagesAttendance/{sys.argv[1]}.jpg",img) #save image


if __name__ == "__main__":
    main()