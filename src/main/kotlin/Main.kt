import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*


fun main(args: Array<String>) {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    var totalLifeGuards=-1
    val startMap = HashMap<Int, Int>()
    val endMap = HashMap<Int, Int>()

    val inputFileAbsolutePath=args[0]

    val `is`: InputStream = FileInputStream(inputFileAbsolutePath)
    Scanner(`is`, StandardCharsets.UTF_8.name()).use { sc ->
        var newLifeguardIndex=1

        while (sc.hasNextLine()) {
            val currentLineString=sc.nextLine()
            //System.out.println(currentLineString)
            if(totalLifeGuards==-1){
                totalLifeGuards=currentLineString.toInt()
                println("Total LifeGuards:"+totalLifeGuards)
            }else{
                startMap[newLifeguardIndex] = currentLineString.substringBefore(" ").toInt()
                endMap[newLifeguardIndex] = currentLineString.substringAfter(" ").toInt()
                println("Start:"+ startMap[newLifeguardIndex]+"    "+"End:"+ endMap[newLifeguardIndex]    )
                newLifeguardIndex += 1
            }
        }

        val sortedStartTimes = startMap.toList().sortedBy { (_, value) -> value}
        val sortedEndTimes = endMap.toList().sortedBy { (_, value) -> value}

        //Find the lifeguard with the smallest alone time
        var totalTimeCovered=0
        val guardAloneMap = HashMap<Int, Int>()
        val currentLifeGuardsOnDuty = mutableSetOf<Int>()
        val lastEndTime=sortedEndTimes[sortedEndTimes.size-1].second

        var startTimeListIndex=0
        var endTimeListIndex=0
        for (i in 0..lastEndTime) {

            if(startTimeListIndex<totalLifeGuards && sortedStartTimes[startTimeListIndex].second==i){
                currentLifeGuardsOnDuty.add(sortedStartTimes[startTimeListIndex].first)
                startTimeListIndex += 1
            }
            if(endTimeListIndex<totalLifeGuards && sortedEndTimes[endTimeListIndex].second==i){
                currentLifeGuardsOnDuty.remove(sortedEndTimes[endTimeListIndex].first)
                endTimeListIndex += 1
            }
            if(currentLifeGuardsOnDuty.size>0){
                totalTimeCovered += 1
            }

            if(currentLifeGuardsOnDuty.size==1){
                currentLifeGuardsOnDuty.forEach {
                    val currentAloneTime=guardAloneMap[it]?:0
                    guardAloneMap[it]=(currentAloneTime+1)
                }
            }

        }

        println("totalTimeCovered:$totalTimeCovered")
        val sortedAloneTimes = guardAloneMap.toList().sortedBy { (_, value) -> value}
        val result=totalTimeCovered-sortedAloneTimes[0].second
        println("result:$result")

        val outputFileName=inputFileAbsolutePath.substringBefore(".in").last()
        val out = PrintStream(FileOutputStream("$outputFileName.out"))
        System.setOut(out)
        println(result)
    }

}