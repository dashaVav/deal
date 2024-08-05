package com.example.deal.mapper;

import com.example.deal.model.Client;
import com.example.deal.model.Passport;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MergeClientService {
    @Mapping(target = "passport",
            expression = "java(mergePassport(a.getPassport(), b.getPassport()))")
    Client mergeClient(@MappingTarget Client a, Client b);

    default Passport mergePassport(Passport a, Passport b) {
        Passport passport = new Passport();
        passport.setSeries(a.getSeries() == null ? b.getSeries() : a.getSeries());
        passport.setNumber(a.getNumber() == null ? b.getNumber() : a.getNumber());
        passport.setIssueBranch(a.getIssueBranch() == null ? b.getIssueBranch() : a.getIssueBranch());
        passport.setIssueDate(a.getIssueDate() == null ? b.getIssueDate() : a.getIssueDate());
        return passport;
    }
}
