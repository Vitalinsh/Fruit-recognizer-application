def getImageByteArray(files):

    if not('image' in files):
        return False

    file = files['image']
    return True, file.read()