package bomtada;

import bomtada.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
public class PolicyHandler{

    @Autowired ClaimRepository claimRepository;
    @Autowired MemberRepository memberRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverHumanResourcesRegistered(@Payload Registered registered)
    {
        System.out.println("##### listener test : " + registered.toJson());
        System.out.println("##### listener appId : " + registered.getAppId());

        if(registered.isMe()){
            Optional<Member> memberOptional = memberRepository.findById(registered.getAppId());
            Member member = memberOptional.get();
            member.setMemberId(registered.getId());
            memberRepository.save(member);
        }

    }
/*
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRegistered_Update(@Payload Registered registered){

        if(registered.isMe()){
            System.out.println("##### listener Update : " + registered.toJson());
        }
    }
*/
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverProcessed(@Payload Processed processed)
    {
        if(processed.isMe()){
            Optional<Claim> claimOptional = claimRepository.findById(processed.getClaimId());
            Claim claim = claimOptional.get();
            claim.setResult(processed.getResult());
            claim.setAmount(processed.getAmount());
            claimRepository.save(claim);
        }

    }


}
