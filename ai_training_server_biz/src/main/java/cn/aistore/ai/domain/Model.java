package cn.aistore.ai.domain;

import cn.aistore.ai.common.TimestampData;
import com.github.houbb.data.factory.api.annotation.DataFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@Entity
@Table(name = "model")
@NoArgsConstructor
@AllArgsConstructor
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    @Builder.Default
    private Integer userId=0;

    /**
     * frequency_penalty是 -2.0 ~ 2.0 之间的数字，
     *
     * 正值会根据新 tokens 在文本中的现有频率对其进行惩罚，从而降低模型逐字重复同一行的可能性（以恐怖故事为例）
     *
     * = -2.0：当早上黎明时，我发现我家现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在现在（频率最高字符是 “现”，占比 44.79%）
     *
     * = -1.0：他总是在清晨漫步在一片森林里，每次漫游每次每次游游游游游游游游游游游游游游游游游游游游游游游游游游游游游（频率最高字符是 “游”，占比 57.69%）
     *
     * = 0.0：当一道阴森的风吹过早晨的小餐馆时，一个被吓得发抖的人突然出现在门口，他的嘴唇上挂满血迹，害怕的店主决定给他一份早餐，却发现他的早餐里满是血渍。（频率最高字符是 “的”，占比 8.45%）
     *
     * = 1.0：一个熟睡的女孩被一阵清冷的风吹得不由自主地醒了，她看到了早上还未到来的黑暗，周围只有像诉说厄运般狂风呼啸而过。（频率最高字符是 “的”，占比 5.45%）
     *
     * = 2.0：每天早上，他都会在露台上坐着吃早餐。柔和的夕阳照耀下，一切看起来安详寂静。但是有一天，当他准备端起早食的时候发现胡同里清冷的风扑进了他的意识中并带来了不安全感…… （频率最高字符是 “的”，占比 4.94%）
     */
    @Column(name = "frequency_penalty")
    private Double frequencyPenalty;

    @DataFactory(max=1, min=0)
    @Column(name = "maintain_context")
    @Min(0)
    @Max(1)
    private Integer maintainContext;

    @Column(name = "max_responses")
    private Long maxResponses;

    @Column(name = "model")
    private String model;

    @Column(name = "name")
//    @NotNull(message = "模型名称不能为空")
    private String name;

    @Column(name = "output_format")
//    @NotNull(message = "模型输出格式不能为空")
    private String outputFormat;

    /**
     * -2.0 ~ 2.0 之间的数字，正值会根据到目前为止是否出现在文本中来惩罚新 tokens，从而增加模型谈论新主题的可能性（以云课堂的广告文案为例）
     *
     * = -2.0：家长们，你们是否为家里的孩子学业的发展而发愁？担心他们的学习没有取得有效的提高？那么，你们可以放心，可以尝试云课堂！它是一个为从幼儿园到高中的学生提供的一个网络平台，可以有效的帮助孩子们提高学习效率，提升学习成绩，帮助他们在学校表现出色！让孩子们的学业发展更加顺利，家长们赶紧加入吧！（抓住一个主题使劲谈论）
     *
     * = -1.0：家长们，你们是否还在为孩子的学习成绩担忧？云课堂给你们带来了一个绝佳的解决方案！我们为孩子提供了专业的学习指导，从幼儿园到高中，我们都能帮助孩子们在学校取得更好的成绩！让孩子们在学习中更轻松，更有成就感！加入我们，让孩子们拥有更好的学习体验！（紧密围绕一个主题谈论）
     *
     * = 0.0：家长们，你们是否担心孩子在学校表现不佳？云课堂将帮助您的孩子更好地学习！云课堂是一个网络平台，为从幼儿园到高中的学生提供了全面的学习资源，让他们可以在学校表现出色！让您的孩子更加聪明，让他们在学校取得更好的成绩，快来云课堂吧！（相对围绕一个主题谈论）
     *
     * = 1.0：家长们，你们的孩子梦想成为最优秀的学生吗？云课堂就是你们的答案！它不仅可以帮助孩子在学校表现出色，还能够提供专业教育资源，助力孩子取得更好的成绩！让你们的孩子一路走向成功，就用云课堂！（避免一个主题谈论的太多）
     *
     * = 2.0：家长们，您有没有想过，让孩子在学校表现出色可不是一件容易的事？没关系！我们为您提供了一个优质的网络平台——云课堂！无论您的孩子是小学生、初中生还是高中生，都能够通过云课堂找到最合适的学习方法，帮助他们在学校取得优异成绩。快来体验吧！（最大程度避免谈论重复的主题）
     */
    @Column(name = "presence_penalty")
    private BigDecimal presencePenalty;

    @Column(name = "role")
//    @NotNull(message = "模型角色不能为空")
    private String role;

    /** 温度也是随机因子，采样温度，介于0和2之间, 控制结果的随机性。 较高的值（如0.8）将使输出更随机，而较低的值（如0.2）将使其更集中和确定性。 */
    @Column(name = "temperature")
//    @NotNull(message = "模型温度不能为空")
    private Double temperature;

    /**
     * 温度采样的替代方案，也称为核采样，它是一个可用于代替 temperature 的参数，对应机器学习中 nucleus sampling（核采样），
     * 如果设置 0.1 意味着只考虑构成前 10% 概率质量的 tokens 。
     *
     * nucleus sampling的主要思想就是：给定一个阈值p，从解码词中挑选出一个cumprob大于p的最小集合。挑选出该最小集合后，
     * 再根据你的设置的sampler进行采样。
     */
    @Column(name = "top_p")
//    @NotNull(message = "模型多样性及保真度不能为空")
    private Double topP;

    @DataFactory(data = TimestampData.class)
    @Column(name = "update_time")
    private Timestamp updateTime;

    @Column(name = "tenant_id")
    @Builder.Default
    private Long tenantId=0L;

    @Column(name = "deleted")
    @Builder.Default
    private Integer deleted=0;

    private Integer stream;
}