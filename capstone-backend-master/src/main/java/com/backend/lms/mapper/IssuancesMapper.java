//package com.backend.lms.mapper;
//
//import com.backend.lms.dto.Issuances.IssuanceInDto;
//import com.backend.lms.dto.Issuances.IssuanceOutDto;
//import com.backend.lms.dto.books.BooksOutDto;
//import com.backend.lms.dto.users.UserDto;
//import com.backend.lms.exception.ResourceNotFoundException;
//import com.backend.lms.model.Books;
//import com.backend.lms.model.Issuances;
//import com.backend.lms.model.Users;
//import com.backend.lms.repository.BooksRepository;
//import com.backend.lms.repository.UsersRepository;
//
//public final class IssuancesMapper {
//
//    public static IssuanceOutDto mapToIssuanceOutDTO(Issuances issuance) {
//        IssuanceOutDto issuanceOutDto = new IssuanceOutDto();
//        issuanceOutDto.setId(issuance.getId());
//        issuanceOutDto.setIssueDate(issuance.getIssueDate());
//        issuanceOutDto.setReturnDate(issuance.getReturnDate());
//        issuanceOutDto.setStatus(issuance.getStatus());
//        issuanceOutDto.setIssuanceType(issuance.getIssuanceType());
//
//        Users userDto = UserMapper.mapToUserDto(issuance.getUsers());
//        issuanceOutDto.setUsers(userDto);
//
//        BooksOutDto bookOutDto = BooksMapper.mapToBookOutDTO(issuance.getBooks());
//        issuanceOutDto.setBooks(bookOutDto);
//
//        return issuanceOutDto;
//    }
//
//    public static Issuances mapToIssuance(IssuanceInDto issuanceInDto, Issuances issuance, UsersRepository usersRepository, BooksRepository booksRepository) {
//        Users user = usersRepository.findById(issuanceInDto.getUserId()).orElseThrow(
//                () -> new ResourceNotFoundException("User", "id", issuanceInDto.getUserId().toString())
//        );
//
//        Books book = booksRepository.findById(issuanceInDto.getBookId()).orElseThrow(
//                () -> new ResourceNotFoundException("Book", "id", issuanceInDto.getBookId().toString())
//        );
//
//        issuance.setUsers(user);
//        issuance.setBooks(book);
//        issuance.setStatus(issuanceInDto.getStatus());
//        issuance.setIssuanceType(issuanceInDto.getIssuanceType());
//        issuance.setReturnDate(issuanceInDto.getReturnDate());
//
//        return issuance;
//    }
//}


package com.backend.lms.mapper;

import com.backend.lms.dto.Issuances.IssuanceInDto;
import com.backend.lms.dto.Issuances.IssuanceOutDto;
import com.backend.lms.dto.books.BooksOutDto;
import com.backend.lms.dto.users.UserDto;
import com.backend.lms.exception.ResourceNotFoundException;
import com.backend.lms.model.Books;
import com.backend.lms.model.Issuances;
import com.backend.lms.model.Users;
import com.backend.lms.repository.BooksRepository;
import com.backend.lms.repository.UsersRepository;

public final class IssuancesMapper {

    public static IssuanceOutDto mapToIssuanceOutDto(Issuances issuance, IssuanceOutDto issuanceOutDto) {
        issuanceOutDto.setId(issuance.getId());
        issuanceOutDto.setIssueDate(issuance.getIssueDate());
        issuanceOutDto.setReturnDate(issuance.getReturnDate());
        issuanceOutDto.setStatus(issuance.getStatus());
        issuanceOutDto.setIssuanceType(issuance.getIssuanceType());

        UserDto userDto = UserMapper.mapToUserDto(issuance.getUsers(), new UserDto());
        issuanceOutDto.setUsers(userDto);

        BooksOutDto bookOutDto = BooksMapper.mapToBookOutDTO(issuance.getBooks(), new BooksOutDto());
        issuanceOutDto.setBooks(bookOutDto);

        return issuanceOutDto;
    }

    public static Issuances mapToIssuance(IssuanceInDto issuanceInDto, Issuances issuance, UsersRepository usersRepository, BooksRepository booksRepository) {
        Users user = usersRepository.findById(issuanceInDto.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", issuanceInDto.getUserId().toString())
        );

        Books book = booksRepository.findById(issuanceInDto.getBookId()).orElseThrow(
                () -> new ResourceNotFoundException("Book", "id", issuanceInDto.getBookId().toString())
        );

        issuance.setUsers(user);
        issuance.setBooks(book);
        issuance.setStatus(issuanceInDto.getStatus());
        issuance.setIssuanceType(issuanceInDto.getIssuanceType());
        issuance.setReturnDate(issuanceInDto.getReturnDate());

        return issuance;
    }
}

