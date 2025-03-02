package com.seecoder.BlueWhale.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CommentVO {

    Integer userId;

    String userName;

    @NotNull(message = "评论order不为空")
    Integer orderId;

    @NotNull(message = "评分不为空")
    @Range(min = 0, max = 5,message = "评分范围在1-5")
    Integer rating;

    @NotNull(message = "评论不为空")
    String comment;

    Date time;
}
