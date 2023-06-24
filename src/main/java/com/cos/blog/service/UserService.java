package com.cos.blog.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.cos.blog.dto.SendTmpPwdDto;
import com.cos.blog.dto.UserRequestDto;
import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;
	private final AuthenticationManager authenticationManager;
	
	private final JavaMailSender javaMailSender;
	
	@Value("${secrect.key}")
	private String secrectKey;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Value("${spring.mail.username}")
	private String sendFrom;



	@Transactional
	public void join(UserRequestDto userDto) {
		User user = User.builder()
				.username(userDto.getUsername())
				.password(encoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.email(userDto.getEmail())
				.role(RoleType.USER)
				.build();

		userRepository.save(user);
	}

	@Transactional
	public void joinUser(User user) {
		userRepository.save(user);
	}

	@Transactional
	public void join(User user) {		
		userRepository.save(user);
	}

	@Transactional
	public void update(UserRequestDto userDto) {
		User persistance = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() -> {
			return new IllegalArgumentException("회원정보 수정 실패: 존재하지 않는 회원입니다.");
		});

		persistance.setPassword(encoder.encode(userDto.getPassword()));
		persistance.setNickname(userDto.getNickname());
	}


	@Transactional
	public void delete(Long user_id) {
		userRepository.deleteById(user_id);
	}
	
	@Transactional(readOnly = true)
	public Map<String, String> validateHandling(BindingResult bindingResult) {
		Map<String, String> validatorResult = new HashMap<>();
		
		for(FieldError error : bindingResult.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		
		return validatorResult;
	}
	
	@Transactional(readOnly = true)
	public Map<String, String> validateHandling(Errors errors){
		Map<String, String> validatorResult = new HashMap<>();
		
		for(FieldError error : errors.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		
		return validatorResult;
	}

	@Transactional
	public void profileImageUpdate(Long user_id, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename();
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
		
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		User user = userRepository.findById(user_id).orElseThrow(() -> {
			return new IllegalArgumentException("프로필 이미지 수정 실패: 존재하지 않는 회원입니다.");
		});
		
		user.setProfile_image_url(imageFileName);
	}
	
	@Transactional
	public void sendTmpPwd(SendTmpPwdDto dto) {
		
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String tmpPwd = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            tmpPwd += charSet[idx];
        }
		
		try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(dto.getEmail());
            message.setFrom(sendFrom);
            message.setSubject("임시 비밀번호 안내 이메일입니다.");
            message.setText("안녕하세요.\n"
            		+ "임시비밀번호 안내 관련 이메일 입니다.\n"
            		+ "임시 비밀번호를 발급하오니 블로그에 접속하셔서 로그인 하신 후\n"
            		+ "반드시 비밀번호를 변경해주시기 바랍니다.\n\n"
            		+ "임시 비밀번호 : " + tmpPwd);
            javaMailSender.send(message);
        } catch (MailParseException e) {
            e.printStackTrace();
        } catch (MailAuthenticationException e) {
            e.printStackTrace();
        } catch (MailSendException e) {
            e.printStackTrace();
        } catch (MailException e) {
            e.printStackTrace();
        }
		
		User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> {
			return new IllegalArgumentException("임시 비밀번호 변경 실패: 사용자 이름을 찾을 수 없습니다.");
		});
		
		user.setPassword(encoder.encode(tmpPwd));
	}

	@Transactional
	public void googleLogin(OAuth2User oauth2User) {
		String provider = "google";
		String username = oauth2User.getAttribute("email");
		String nickname = oauth2User.getAttribute("name");
		String email = oauth2User.getAttribute("email");
		String role = RoleType.USER.toString();

		User user = userRepository.findByUsername(username).orElse(null);

		if (user == null) {
			// 신규 사용자일 경우 회원가입 처리
			user = User.builder()
					.username(username)
					.nickname(nickname)
					.email(email)
					.role(RoleType.valueOf(role))
					.oauth(provider)
					.build();

			userRepository.save(user);
		}

		// 로그인 처리
		Authentication authentication =
				new UsernamePasswordAuthenticationToken(oauth2User, null, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}