package binea.reporters

import binea.Timing
import org.gradle.BuildResult
import org.gradle.api.logging.Logger

/**
 * Created by xubinggui on 4/10/16.
 //                            _ooOoo_  
 //                           o8888888o  
 //                           88" . "88  
 //                           (| -_- |)  
 //                            O\ = /O  
 //                        ____/`---'\____  
 //                      .   ' \\| |// `.  
 //                       / \\||| : |||// \  
 //                     / _||||| -:- |||||- \  
 //                       | | \\\ - /// | |  
 //                     | \_| ''\---/'' | |  
 //                      \ .-\__ `-` ___/-. /  
 //                   ___`. .' /--.--\ `. . __  
 //                ."" '< `.___\_<|>_/___.' >'"".  
 //               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
 //                 \ \ `-. \_ __\ /__ _/ .-` / /  
 //         ======`-.____`-.___\_____/___.-`____.-'======  
 //                            `=---='  
 //  
 //         .............................................  
 //                  佛祖镇楼                  BUG辟易 

 */
public class SummaryReporter extends AbstractBuildTimeTrackerReporter {

    def static final UNICODE_SQUARE = " "
    def static final ASCII_SQUARE = " "
    def static final FILL = " "

    String barStyle
    boolean successOutput

    SummaryReporter(Map<String, String> options,
            Logger logger) {
        super(options, logger)

        barStyle = getOption("barstyle", "unicode")
        successOutput = Boolean.parseBoolean(getOption("successOutput", "true"))
    }

    @Override
    def run(List<Timing> timings) {
        if(timings.size() == 0) return

        def threshold = getOption("threshold", "50").toInteger()

        if(getOption("ordered", "false").toBoolean()) {
            timings = timings.sort(false, {it.ms})
        }

        logger.quiet("== Build Time Summary ==")
        formatTable(timings, threshold)
    }

    @Override
    void onBuildResult(BuildResult result) {
        if(!successOutput) {
            return
        }

        logger.lifecycle("")

        if(result.failure != null) {
            logger.error("== BUILD FAILED ==")
        } else {
            logger.lifecycle("== BUILD SUCCESSFUL ==")
        }
    }

    def formatTable(List<Timing> timings, int threshold) {
        def total = timings.sum {t -> t.ms}
        def longestTaskName = timings.collect {it.path.length()}.max()
        def longestTiming = timings*.ms.max()
        def maxColumns = (new TerminalInfo()).getWidth(80)

        def maxBarWidth
        if(longestTaskName > maxColumns / 2) {
            maxBarWidth = (maxColumns - 20) / 2
        }else {
            maxBarWidth = maxColumns - (longestTaskName + 20)
        }

        for(timing in timings) {
            if(timing.ms >= threshold) {
                logger.quiet(sprintf("%s %s (%s)", createBar(timing.ms / total, timing.ms / longestTiming, maxBarWidth),
                shortenTaskName(timing.path, maxBarWidth),
                        FormattingUtils.formatDuration(timing.ms)))
            }
        }
    }

    def static shortenTaskName(String taskName, def max) {
        if (taskName.length() < max) { return taskName }

        int partLength = Math.floor((max - 3) / 2) as int
        def start = taskName.substring(0, partLength + 1)
        def end = taskName.substring(taskName.length() - partLength)

        start.trim() + '…' + end.trim()
    }

    def createBar(def fracOfTotal, def fracOfMax, def max) {
        def symbol = barStyle = "ascii" ? ASCII_SQUARE : UNICODE_SQUARE

        def rounedTotal = Math.round(fracOfMax * 100)
        def barLength = Math.ceil(max * fracOfMax)
        def bar = FILL * (max - barLength) + symbol * (barLength - 1)
        def formatted = (rounedTotal < 10 ? " " : "") + rounedTotal
        return (barStyle != "none" ? (bar + " ") : "") + formatted + '%'
    }
}