let localVideo = document.getElementById("local-video")
let remoteVideo = document.getElementById("remote-video")

localVideo.style.opacity = 0
remoteVideo.style.opacity = 0

localVideo.onplaying = () => { localVideo.style.opacity = 1 }
remoteVideo.onplaying = () => { remoteVideo.style.opacity = 1 }

let peer
function init(userId, name) {
    button.innerText = name

    peer = new Peer(userId, {
        host: '192.168.1.103',
        port: 9000,
        path: '/videocallapp'
    })

    peer.on('open' ,() => {
        Android.onPeerConnected()
    })

    listen()
}


let localStream
function listen() {
    peer.on('call', (call) => {

        navigator.mediaDevices.getUserMedia({
            audio: true, 
            video: true
        }).then((stream) => {
            localVideo.srcObject = stream
            localStream = stream

            call.answer(stream)
            call.on('stream', (remoteStream) => {
                remoteVideo.srcObject = remoteStream

                remoteVideo.className = "primary-video"
                localVideo.className = "secondary-video"

            })

        })
        
    })
}

function startCall(otherUserId) {
    navigator.mediaDevices.getUserMedia({
        audio: true,
        video: true
    }).then((stream) => {
        button.innerText = otherUserId
        localVideo.srcObject = stream
        localStream = stream

        const call = peer.call(otherUserId, stream)
        call.on('stream' , (remoteStream) => {
            remoteVideo.srcObject = remoteStream
            remoteVideo.className = "primary-video"
            localVideo.className = "secondary-video"
        })

    })
}



function toggleVideo(b) {
    if (b == "true") {
        localStream.getVideoTracks()[0].enabled = true
    } else {
        localStream.getVideoTracks()[0].enabled = false
    }
}

function toggleAudio(b) {
    if (b == "true") {
        localStream.getAudioTracks()[0].enabled = true
    } else {
        localStream.getAudioTracks()[0].enabled = false
    }
} 