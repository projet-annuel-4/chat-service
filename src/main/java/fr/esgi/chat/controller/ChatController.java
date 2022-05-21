package fr.esgi.chat.controller;


import fr.esgi.chat.dto.FriendProfileResponse;
import fr.esgi.chat.dto.MessageResponse;
import fr.esgi.chat.mapper.FriendMapper;
import fr.esgi.chat.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.Collections.emptyList;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final FriendMapper friendMapper;
    private final MessageMapper messageMapper;

    @GetMapping
    public ResponseEntity<List<FriendProfileResponse>> getFriends(@RequestParam(value="from", required = false) String from,@RequestParam(value="user-email", required = true) String userEmail){
        return ResponseEntity.ok(friendMapper.getFriends(userEmail,from));
    }

    @PostMapping
    public ResponseEntity<FriendProfileResponse> startConversation(@RequestParam(value="user-email", required = true) String userEmail,@RequestParam(value="friend-email", required = true) String friendEmail){
        return ResponseEntity.ok(friendMapper.newConversation(userEmail,friendEmail));
    }
    @PostMapping("/messages")
    public ResponseEntity<List<MessageResponse>>  getAllMessages(@RequestBody List<Long> ids,
                                                                 @RequestParam(value="from", required = false) String from
            ,@RequestParam(value="user-email", required = true) String userEmail){
        return ResponseEntity.ok(messageMapper.getAllMessages(userEmail,ids,from));
    }

    @PatchMapping("/{cid}/block")
    public ResponseEntity<FriendProfileResponse> blockUser(@PathVariable("cid") Long id,@RequestParam(value="user-email", required = true) String userEmail){
        return ResponseEntity.ok(friendMapper.blockConversation(userEmail,id));
    }

    @PatchMapping("/{cid}/unblock")
    public ResponseEntity<FriendProfileResponse> unblockUser(@PathVariable("cid")  Long id,@RequestParam(value="user-email", required = true) String userEmail){
        return ResponseEntity.ok(friendMapper.unblockConversation(userEmail,id));
    }
    @GetMapping("/{cid}/messages")
    public ResponseEntity<List<MessageResponse>>  getAllMessages(@PathVariable("cid")  Long id,
                                                                 @RequestParam(value="from", required = false) String from
            ,@RequestParam(value="user-email", required = true) String userEmail){
        return ResponseEntity.ok(messageMapper.getMessagesByChat(userEmail,id,from));
    }

    @PostMapping("/{cid}/messages/text")
    public ResponseEntity<MessageResponse>  createMessageText(@RequestHeader HttpHeaders headers, @PathVariable("cid")  Long id,
                                                              @RequestParam(value="content", required = false) String content,
                                                              @RequestParam(value="user-email", required = true) String userEmail){
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        return ResponseEntity.ok(messageMapper.createMessage(userEmail,id,content,emptyList()));
    }

    @PostMapping(path = "/{cid}/messages/files", consumes = "multipart/form-data")
    public ResponseEntity<MessageResponse>  createMessageFile(@PathVariable("cid")  Long id,
                                                              @RequestParam(value="content", required = false) String content,
                                                              @RequestParam("files") List<MultipartFile> files
            ,@RequestParam(value="user-email", required = true) String userEmail){
        return ResponseEntity.ok(messageMapper.createMessage(userEmail,id,content,files));
    }
    @PutMapping("/{cid}/messages/read")
    public ResponseEntity<List<MessageResponse>>  readMessage(@RequestBody List<Long> ids,
                                                              @PathVariable("cid") Long convId
            ,@RequestParam(value="user-email", required = true) String userEmail){
        return ResponseEntity.ok(messageMapper.updateMessages(userEmail,ids,convId));
    }


}
